package com.filavents.books_highlights.utils;

import com.filavents.books_highlights.configs.Database;
import com.filavents.books_highlights.dto.JsonResponse;
import com.filavents.books_highlights.entity.Note;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.persistence.EntityManager;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotesIndexer {

  private static Directory directory = null;

  static Logger logger = LoggerFactory.getLogger(NotesIndexer.class);

  private NotesIndexer() {
  }

  static {
    try {
      directory = FSDirectory.open(Paths.get("index"));
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  public static boolean add(IndexWriter indexWriter, String id, String note, String bookId, String bookTitle) throws IOException {
    Document doc = new Document();
    StringField idField = new StringField("id", id, Field.Store.YES);
    TextField textFieldNote = new TextField("note", note, Field.Store.YES);
    StringField idFieldBookId = new StringField("bookId", bookId, Field.Store.YES);
    TextField textFieldBookTitle = new TextField("bookTitle", bookTitle, Field.Store.YES);
    doc.add(idField);
    doc.add(textFieldNote);
    doc.add(idFieldBookId);
    doc.add(textFieldBookTitle);

    indexWriter.addDocument(doc);
    return true;
  }

  public static boolean initIndex() throws IOException {
    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    List<Note> notes = entityManager.createQuery("SELECT n FROM Note n GROUP BY n.getGoogleBookNote", Note.class)
      .getResultList();
    entityManager.close();

    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
    IndexWriter indexWriter = new IndexWriter(directory, config);

    indexWriter.deleteAll();

    for (Note note : notes) {
      try {
        add(
          indexWriter,
          note.getId().toString(),
          note.getGetGoogleBookNote(),
          String.valueOf(note.getBook().getId()),
          note.getBook().getBookTitle()
        );
        logger.info("Added note ID: " + note.getId());
      } catch (IOException e) {
        logger.error(e.getMessage());
        indexWriter.close();
        return false;
      }
    }
    indexWriter.close();
    logger.info("Index created");
    return true;
  }

  public static JsonResponse search(String queryString) throws ParseException, IOException {
    IndexReader reader = DirectoryReader.open(directory);
    IndexSearcher searcher = new IndexSearcher(reader);

    MultiFieldQueryParser parser = new MultiFieldQueryParser(
      new String[]{"note", "bookTitle"},
      new StandardAnalyzer()
    );
    Query query = parser.parse(queryString + "~1");
    TopDocs results = searcher.search(query, 30);

    List<Map<String, String>> searchResultsList = new ArrayList<>();
    for (ScoreDoc scoreDoc : results.scoreDocs) {
      Document doc = searcher.doc(scoreDoc.doc);

      Map<String, String> searchResultsMap = new HashMap<>();
      searchResultsMap.put("noteId", doc.get("id"));
      searchResultsMap.put("noteText", doc.get("note"));
      searchResultsMap.put("bookId", doc.get("bookId"));
      searchResultsMap.put("bookTitle", doc.get("bookTitle"));

      searchResultsList.add(searchResultsMap);
    }
    reader.close();
    return new JsonResponse(true, searchResultsList);
  }

  private static List<Map<String, String>> cleanDuplicateNoteText(List<Map<String, String>> searchResultsList) {
    List<Map<String, String>> searchResultsListCleaned = new ArrayList<>();
    for (Map<String, String> searchResult : searchResultsList) {
      boolean isDuplicate = false;
      for (Map<String, String> cleanedResult : searchResultsListCleaned) {
        if (cleanedResult.get("noteText").equals(searchResult.get("noteText"))) {
          isDuplicate = true;
          break;
        }
      }
      if (!isDuplicate) {
        searchResultsListCleaned.add(searchResult);
      }
    }
    return searchResultsListCleaned;
  }
}
