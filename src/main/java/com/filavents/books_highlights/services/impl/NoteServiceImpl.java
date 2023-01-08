package com.filavents.books_highlights.services.impl;

import com.filavents.books_highlights.configs.Database;
import com.filavents.books_highlights.entity.Book;
import com.filavents.books_highlights.entity.Note;
import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.utils.GoogleApi;
import com.google.api.services.drive.Drive;
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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NoteServiceImpl implements NoteService {

  static Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

  @Override
  public List<Book> getAllBooks() {
    return null;
  }

  @Override
  public Book getBookById(Long id) {
    return null;
  }

  @Override
  public boolean syncBooks() throws GeneralSecurityException, IOException {

    Drive driveService = GoogleApi.getDriveService();
    FileList result = GoogleApi.getFilesListByFolderId(driveService, "1lK4_eUjkmJgXnyTMaN7MYECMWr7jceUi");

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

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            driveService.files().export(file.getId(), "application/zip").executeMediaAndDownloadTo(outputStream);

            ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
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

      CompositeFuture.all(futures).onComplete(ar -> {
        if (ar.succeeded()) {
          logger.info("All files processed");
        } else {
          logger.error("Error processing files", ar.cause());
        }
      });

      return true;
    }
  }

  private static void processContent(ZipInputStream zipInputStream, File file) throws IOException {
    Document doc = Jsoup.parse(new String(zipInputStream.readAllBytes()));
    String title = doc.select("body > table:nth-child(4) > tbody > tr > td:nth-child(2) > h1 > span").text();
    String author = doc.select("body > table:nth-child(4) > tbody > tr > td:nth-child(2) > p:nth-child(2) > span").text();
    String notesCount = doc.select("body > h1:nth-child(12) > span").text();

    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    entityManager.getTransaction().begin();

    Book book = new Book();
    book.setGoogleBookTitle(title);
    book.setGoogleBookAuthor(author);
    book.setGoogleBookNotesCount(notesCount);
    book.setGoogleBookId(file.getId());

    doc.select("body > table > tbody > tr > td > table > tbody > tr")
      .forEach(element -> {
        String noteBody = element.select("td:nth-child(2) > p:nth-child(1) > span").text();
        String noteDate = element.select("td:nth-child(2) > p:nth-child(3) > span").text();
        String noteUrl = element.select("td:nth-child(3) > p > span > a").attr("href");

        Note note = new Note();
        note.setGetGoogleBookNote(noteBody);
        note.setNoteUrl(noteUrl);
        note.setGoogleBookDate(noteDate);
        note.setGoogleBookId(file.getId());
        note.setBook(book);

        book.getNotes().add(note);

      });

    entityManager.persist(book);
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}

