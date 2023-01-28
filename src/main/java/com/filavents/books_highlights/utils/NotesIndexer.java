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
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.DistinctValuesCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class NotesIndexer {

  private static IndexWriterConfig config = null;
  private static Directory directory = null;

  static Logger logger = LoggerFactory.getLogger(NotesIndexer.class);

  private NotesIndexer() {
  }

  static {
    try {
      directory = FSDirectory.open(Paths.get("index"));
      config = new IndexWriterConfig(new StandardAnalyzer());
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  public static void main(String[] args) throws Exception {
    //initIndex();
    System.out.println(search("agile best practice").asMap().toString());
    //indexNotes();
  }

  public static boolean add(IndexWriter indexWriter, String id, String note, String bookTitle) throws IOException {
    // create a new document and add fields to it
    Document doc = new Document();
    StringField idField = new StringField("id", id, Field.Store.YES);
    TextField textFieldNote = new TextField("note", note, Field.Store.YES);
    TextField textFieldBookTitle = new TextField("bookTitle", bookTitle, Field.Store.YES);
    doc.add(idField);
    doc.add(textFieldNote);
    doc.add(textFieldBookTitle);

    // add the document to the index
    indexWriter.addDocument(doc);

    return true;
  }

  public static boolean initIndex() throws IOException {
    EntityManager entityManager = Database.getEntityManagerFactory().createEntityManager();
    List<Note> notes = entityManager.createQuery("SELECT n FROM Note n GROUP BY n.getGoogleBookNote", Note.class)
      .getResultList();
    entityManager.close();

    IndexWriter indexWriter = new IndexWriter(directory, config);

    // Delete existing index
    indexWriter.deleteAll();

    for (Note note : notes) {
      try {
        add(indexWriter, note.getId().toString(), note.getGetGoogleBookNote(), note.getBook().getBookTitle());
        logger.info("Added note " + note.getGetGoogleBookNote());
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

    // create a query parser
    //QueryParser parser = new QueryParser("note", new StandardAnalyzer());
    MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"note"}, new StandardAnalyzer());

    // parse the query
    Query query = parser.parse(queryString);

    // search for the query
    TopDocs results = searcher.search(query, 10); // number of maximum result to return

    List<Map> searchResultsList = new ArrayList<>();

    // iterate over the results and print the documents
    for (ScoreDoc scoreDoc : results.scoreDocs) {
      Document doc = searcher.doc(scoreDoc.doc);

      Map<String, String> searchResultsMap = new HashMap<>();
      searchResultsMap.put("noteId", doc.get("id"));
      searchResultsMap.put("noteText", doc.get("note"));
      searchResultsMap.put("bookTitle", doc.get("bookTitle"));

      searchResultsList.add(searchResultsMap);
    }

    // close the index reader
    reader.close();

    return new JsonResponse(true, searchResultsList);
  }

  private static List<Map> cleanDuplicateNoteText(List<Map> searchResultsList) {
    List<Map> searchResultsListCleaned = new ArrayList<>();
    for (Map searchResult : searchResultsList) {
      if (!searchResultsListCleaned.contains(searchResult.get("noteText"))) {
        searchResultsListCleaned.add(searchResult);
      }
    }
    return searchResultsListCleaned;
  }
}
