package com.filavents.books_highlights;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* class to demonstarte use of Drive files list API */
public class App {
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";

  /**
   * Global instance of the JSON factory.
   */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /**
   * Directory to store authorization tokens for this application.
   */
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

    // Load client secrets.
    InputStream in = App.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
      .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
      .setAccessType("offline")
      .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    //returns an authorized Credential object.
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  public static void main(String... args) throws IOException, GeneralSecurityException {

    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
      .setApplicationName(APPLICATION_NAME)
      .build();

    // Print the names and IDs for up to 10 files.
    FileList result = service.files().list()
      .setQ("'1lK4_eUjkmJgXnyTMaN7MYECMWr7jceUi' in parents")
      .setOrderBy("modifiedTime desc")
      .setPageSize(1)
      .setFields("nextPageToken, files(id, name)")
      .execute();

    List<File> files = result.getFiles();
    if (files == null || files.isEmpty()) {
      System.out.println("No files found.");
    } else {
      System.out.println("Files:");
      for (File file : files) {
        //System.out.printf("%s (%s)\n", file.getName(), file.getId());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.files().export(file.getId(), "application/zip").executeMediaAndDownloadTo(outputStream);

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
          // if extension is .html
          if (zipEntry.getName().endsWith(".html")) {
            System.out.println(zipEntry.getName());
            Document doc = Jsoup.parse(new String(zipInputStream.readAllBytes()));
            System.out.println(doc.select("body > table:nth-child(4) > tbody > tr > td:nth-child(2) > h1 > span").text());
            System.out.println(doc.select("body > table:nth-child(4) > tbody > tr > td:nth-child(2) > p:nth-child(2) > span").text());
            System.out.println(doc.select("body > h1:nth-child(12) > span").text());
            System.out.println("\n");
            doc.select("body > table > tbody > tr > td > table > tbody > tr")
              .forEach(element -> {
                System.out.println(element.select("td:nth-child(2) > p:nth-child(1) > span").text());
                System.out.println(element.select("td:nth-child(2) > p:nth-child(3) > span").text());
                System.out.println(element.select("td:nth-child(3) > p > span > a").attr("href"));
                System.out.println("\n");
              });
            break;
          }
          zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.closeEntry();
        zipInputStream.close();
      }
    }
  }

}
