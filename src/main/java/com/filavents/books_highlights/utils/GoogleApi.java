package com.filavents.books_highlights.utils;

import com.filavents.books_highlights.App;
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
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleApi {

  private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";
  private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {


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

  /**
   * Create an authorized Credential object.
   *
   * @return
   * @throws GeneralSecurityException
   * @throws IOException
   */
  private static Drive getDriveService() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
      .setApplicationName(APPLICATION_NAME)
      .build();
  }

  /**
   * Get file list from google drive
   *
   * @param driveService
   * @param folderId
   * @param pageSize
   * @return
   * @throws IOException
   */
  public static FileList getFilesListByFolderId(String folderId) throws IOException, GeneralSecurityException {
    return getDriveService().files().list()
      .setQ("'" + folderId + "' in parents")
      .setOrderBy("modifiedTime desc")
      //.setPageSize(10)
      .setFields("nextPageToken, files(id, name)")
      .execute();
  }

  /**
   * Download file from google drive as ZIP
   * @param fileId
   * @param outputStream
   * @throws IOException
   * @throws GeneralSecurityException
   */
  public static void getFileById(String fileId, ByteArrayOutputStream outputStream) throws IOException, GeneralSecurityException {
    getDriveService().files().export(fileId, "application/zip").executeMediaAndDownloadTo(outputStream);
  }
}
