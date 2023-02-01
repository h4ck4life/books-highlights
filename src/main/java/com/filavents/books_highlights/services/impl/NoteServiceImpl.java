package com.filavents.books_highlights.services.impl;

import com.filavents.books_highlights.configs.Database;
import com.filavents.books_highlights.entity.Book;
import com.filavents.books_highlights.entity.Note;
import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.utils.GoogleApi;
import com.filavents.books_highlights.utils.NotesIndexer;
import com.filavents.books_highlights.utils.Notification;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.persistence.EntityManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NoteServiceImpl implements NoteService {

  static Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

  @Override
  public List<Book> getAllBooks() {
    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    List<Book> books = entityManager.createQuery("SELECT b FROM Book b ORDER BY driveModifiedTime desc", Book.class)
      .getResultList();
    entityManager.close();
    if (books == null) {
      return Collections.emptyList();
    } else {
      return books;
    }
  }

  @Override
  public List<Note> getNotesByBookId(Long id) {
    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    List<Note> notes = entityManager.createQuery("SELECT n FROM Note n WHERE n.book.id = :id ORDER BY id ASC", Note.class)
      .setParameter("id", id)
      .getResultList();
    entityManager.close();
    if (notes == null) {
      return Collections.emptyList();
    } else {
      return notes;
    }
  }

  @Override
  public boolean syncNotesByBookId(String bookId) throws GeneralSecurityException, IOException {

    logger.info("Syncing notes for bookId: " + bookId);

    // Delete current notes by book Id before updating with new notes from Gdrive
    Book book = getBookAndDeleteCurrentNotes(bookId);

    // Get book by id
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    GoogleApi.getFileById(book.getDriveId(), outputStream);

    // Unzip file
    ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
    ZipEntry zipEntry = zipInputStream.getNextEntry();

    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    if (zipEntry != null) {
      entityManager.getTransaction().begin();
      entityManager.createQuery("UPDATE Book b set b.driveModifiedTime = :driveModifiedTime WHERE b.id = :id")
        .setParameter("id", book.getId())
        .setParameter("driveModifiedTime", zipEntry.getLastModifiedTime().toInstant().toEpochMilli())
        .executeUpdate();
      entityManager.getTransaction().commit();
    }

    // Iterate over all files in zip
    while (zipEntry != null) {
      if (zipEntry.getName().endsWith(".html")) {
        Document doc = Jsoup.parse(new String(zipInputStream.readAllBytes()));
        doc.select("body > table > tbody > tr > td > table > tbody > tr")
          .forEach(element -> {
            String noteBody = element.select("td:nth-child(2) > p:nth-child(1) > span").text();
            String noteDate = element.select("td:nth-child(2) > p:nth-child(3) > span").text();
            String noteUrl = element.select("td:nth-child(3) > p > span > a").attr("href");

            Note note = new Note();
            note.setGetGoogleBookNote(noteBody);
            note.setNoteUrl(noteUrl);
            note.setGoogleBookId(bookId);
            note.setBook(book);

            logger.info(noteBody);

            entityManager.getTransaction().begin();
            entityManager.persist(note);
            entityManager.getTransaction().commit();
          });
        break;
      }
      zipEntry = zipInputStream.getNextEntry();
    }
    zipInputStream.closeEntry();
    zipInputStream.close();

    entityManager.close();

    startNotesIndexing();

    return true;
  }

  private void clearAllBooks() {
    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.createQuery("DELETE FROM Note").executeUpdate();
    entityManager.createQuery("DELETE FROM Book").executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  private Book getBookAndDeleteCurrentNotes(String bookId) {
    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();

    // Get book by id
    Book book = entityManager.createQuery("SELECT b FROM Book b WHERE b.id = :id", Book.class)
      .setParameter("id", bookId)
      .getSingleResult();

    // Delete existing notes by driveId
    entityManager.getTransaction().begin();
    entityManager.createQuery("DELETE FROM Note n WHERE n.book.id = :id")
      .setParameter("id", book.getId())
      .executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();

    return book;
  }

  @Override
  public boolean syncBooks() throws GeneralSecurityException, IOException {

    long startTime = System.currentTimeMillis();

    logger.info("Syncing books..");
    clearAllBooks();

    // Get all books from Gdrive by folder Id
    FileList result = GoogleApi.getFilesListByFolderId("1lK4_eUjkmJgXnyTMaN7MYECMWr7jceUi");

    // iterate over all books
    List<File> files = result.getFiles();
    if (files == null || files.isEmpty()) {
      logger.info("No files found.");
      return false;

    } else {
      List<Future> futures = new ArrayList<>();

      for (File file : files) {
        futures.add(Future.future(promise -> {
          try {
            logger.info("File name: " + file.getName());

            // Get book by id
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GoogleApi.getFileById(file.getId(), outputStream);

            // Unzip file
            ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            // Iterate over all files in zip
            while (zipEntry != null) {
              if (zipEntry.getName().endsWith(".html")) {
                processContent(zipInputStream, file);
                break;
              }
              zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
            promise.complete();

          } catch (Exception e) {
            promise.fail(e);
          }
        }));
      }

      // Composite future partial success
      CompositeFuture.any(futures).onComplete(ar -> {
        if (ar.succeeded()) {
          logger.info("All files processed");
        } else {
          logger.error("Some files failed to process", ar.cause());
          Notification.sendEmail(
            "Some books sync failed",
            "Completed in " + msToMinutes(System.currentTimeMillis() - startTime) + " minutes",
            System.getenv("EMAIL_TO")
          );
        }
      });

      startNotesIndexing();
      GoogleApi.syncBookCovers();

      Notification.sendEmail(
        "Books sync completed",
        "Completed in " + msToMinutes(System.currentTimeMillis() - startTime) + " minutes",
        System.getenv("EMAIL_TO")
      );

      return true;
    }
  }

  private void startNotesIndexing() {
    // Init note indexing
    try {
      boolean isCompleted = NotesIndexer.initIndex();
      if (isCompleted) {
        logger.info("Indexing completed");
      } else {
        logger.error("Indexing failed");
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void processContent(ZipInputStream zipInputStream, File file) throws IOException {
    Document doc = Jsoup.parse(new String(zipInputStream.readAllBytes()));

    // Extract book info
    String title = doc.select("body > table:nth-child(4) > tbody > tr > td:nth-child(2) > h1 > span").text();
    String author = doc.select("body > table:nth-child(4) > tbody > tr > td:nth-child(2) > p:nth-child(2) > span").text();
    String notesCount = doc.select("body > h1:nth-child(12) > span").text();

    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    entityManager.getTransaction().begin();

    Book book = new Book();
    book.setBookTitle(title);
    book.setBookAuthor(author);
    book.setBookNotesCount(notesCount);
    book.setDriveId(file.getId());
    book.setDriveFileName(file.getName());
    book.setDriveCreatedTime(file.getCreatedTime().getValue());
    book.setDriveModifiedTime(file.getModifiedTime().getValue());
    book.setDriveMimeType(file.getMimeType());
    book.setDriveFileSize(file.getSize() != null ? file.getSize() : 0);
    book.setDriveWebViewLink(file.getWebViewLink());
    book.setDriveFileExtension(file.getFileExtension());

    // Extract notes from book
    doc.select("body > table > tbody > tr > td > table > tbody > tr")
      .forEach(element -> {
        String noteBody = element.select("td:nth-child(2) > p:nth-child(1) > span").text();
        String noteDate = element.select("td:nth-child(2) > p:nth-child(3) > span").text();
        String noteUrl = element.select("td:nth-child(3) > p > span > a").attr("href");

        Note note = new Note();
        note.setGetGoogleBookNote(noteBody);
        note.setNoteUrl(noteUrl);
        note.setGoogleBookId(file.getId());
        note.setBook(book);

        book.getNotes().add(note);
      });

    entityManager.persist(book);
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  private String msToMinutes(long ms) {
    return String.format("%02d:%02d",
      TimeUnit.MILLISECONDS.toMinutes(ms),
      TimeUnit.MILLISECONDS.toSeconds(ms) -
        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
    );
  }
}

