package com.filavents.books_highlights.utils;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


public class NotesIndexer {

  private static IndexWriterConfig config = null;
  private static Directory directory = null;
  private static IndexWriter indexWriter = null;
  private static IndexReader reader = null;
  private static IndexSearcher searcher = null;

  static Logger logger = LoggerFactory.getLogger(NotesIndexer.class);

  private NotesIndexer() {
  }

  static {
    try {
      directory = FSDirectory.open(Paths.get("index"));
      config = new IndexWriterConfig(new StandardAnalyzer());
      indexWriter = new IndexWriter(directory, config);
      reader = DirectoryReader.open(directory);
      searcher = new IndexSearcher(reader);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  public static void main(String[] args) throws Exception {
    search("library");
    //indexNotes();
  }

  public static IndexWriter getIndexWriter() {
    return indexWriter;
  }

  public static boolean add() throws IOException {
    // create a new document and add fields to it
    Document doc = new Document();
    StringField idField = new StringField("id", "123", Field.Store.YES);
    TextField textField = new TextField("text", "Lucene is a powerful search library", Field.Store.YES);
    doc.add(idField);
    doc.add(textField);

    // add the document to the index
    indexWriter.addDocument(doc);

    // close the index writer
    indexWriter.close();

    return true;
  }

  public static boolean loadIndex() {
    return false;
  }

  public static List<String> search(String queryString) throws ParseException, IOException {
    // create a query parser
    QueryParser parser = new QueryParser("text", new StandardAnalyzer());

    // parse the query
    Query query = parser.parse(queryString);

    // search for the query
    TopDocs results = searcher.search(query, 10); // 10 is the number of maximum result to return

    // iterate over the results and print the documents
    for (ScoreDoc scoreDoc : results.scoreDocs) {
      Document doc = searcher.doc(scoreDoc.doc);
      System.out.println("ID: " + doc.get("id"));
      System.out.println("Text: " + doc.get("text"));
    }

    // close the index reader
    reader.close();

    return null;
  }
}
