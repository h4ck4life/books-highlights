package com.filavents.books_highlights.utils;

import com.filavents.books_highlights.App;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenRequest;
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
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

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

  static Logger logger = LoggerFactory.getLogger(GoogleApi.class);

  private static GoogleAuthorizationCodeFlow getGoogleAuthorizationFlow() throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    InputStream in = App.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
      .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
      .setAccessType("offline")
      .build();
  }

  public static Credential getCredentials() throws IOException, GeneralSecurityException {
    return getGoogleAuthorizationFlow().loadCredential(System.getenv("USER"));
  }

  public static boolean setCredentials(String code) {
    try {
      TokenRequest tokenRequest = getGoogleAuthorizationFlow().newTokenRequest(code).setRedirectUri(System.getenv("HOSTNAME") + "/api/oauth2callback");
      Credential credential = getGoogleAuthorizationFlow().createAndStoreCredential(tokenRequest.execute(), System.getenv("USER"));
      return credential != null;
    } catch (GeneralSecurityException | IOException e) {
      logger.error(e.getMessage());
      return false;
    }
  }

  public static String getAuthorizeUrl() throws IOException, GeneralSecurityException {
    return getGoogleAuthorizationFlow().newAuthorizationUrl().setRedirectUri(System.getenv("HOSTNAME") + "/api/oauth2callback").build();
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
    return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
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
      //.setPageSize(2)
      .setFields("nextPageToken, files(id, name, mimeType, modifiedTime, createdTime, name, size, fullFileExtension, webViewLink)")
      .execute();
  }

  /**
   * Download file from google drive as ZIP
   *
   * @param fileId
   * @param outputStream
   * @throws IOException
   * @throws GeneralSecurityException
   */
  public static void getFileById(String fileId, ByteArrayOutputStream outputStream) throws IOException, GeneralSecurityException {
    getDriveService().files().export(fileId, "application/zip").executeMediaAndDownloadTo(outputStream);
  }
}
