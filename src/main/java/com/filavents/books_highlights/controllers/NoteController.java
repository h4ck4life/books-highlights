package com.filavents.books_highlights.controllers;

import com.filavents.books_highlights.dto.JsonResponse;
import com.filavents.books_highlights.entity.Book;
import com.filavents.books_highlights.entity.Note;
import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.services.impl.NoteServiceImpl;
import com.filavents.books_highlights.utils.GoogleApi;
import com.filavents.books_highlights.utils.NotesIndexer;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public class NoteController {

  private static final NoteService noteService = new NoteServiceImpl();
  static OkHttpClient client = new OkHttpClient();
  static Dotenv dotenv = Dotenv.load();

  private NoteController() {

  }

  static Logger logger = LoggerFactory.getLogger(NoteController.class);

  public static void syncBookCovers(RoutingContext ctx) {
    ctx.response()
        .setStatusCode(200)
        .putHeader("content-type", "application/json")
        .end(Buffer.buffer(new JsonObject().put("success", true).encode()));

    Future.future(promise -> {
      boolean result = GoogleApi.syncBookCovers();
      promise.complete(result);
    });
  }

  public static void syncBooks(RoutingContext ctx) {
    boolean isOk = verifyPinViaGoogleAuthenticator(ctx);
    if (isOk) {
      try {
        if (GoogleApi.getCredentials() != null) {
          ctx.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .end(Buffer.buffer(new JsonObject().put("success", true).encode()));

          Future.future(promise -> {
            boolean result = false;
            try {
              result = noteService.syncBooks();
            } catch (Exception e) {
              logger.error("Error syncing books: " + e.getMessage());
              throw new RuntimeException(e);
            }
            promise.complete(result);
          });
        } else {
          ctx.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .end(
                  Buffer.buffer(
                      new JsonObject()
                          .put("success", false)
                          .put("redirect", true)
                          .put("redirectUrl", GoogleApi.getAuthorizeUrl())
                          .encode()));
        }
      } catch (GeneralSecurityException | IOException e) {
        ctx.response()
            .setStatusCode(550)
            .putHeader("content-type", "application/json")
            .end(Buffer.buffer(new JsonObject().put("success", false).encode()));
      }
    } else {
      ctx.response()
          .setStatusCode(401)
          .putHeader("content-type", "application/json")
          .end(Buffer.buffer(new JsonObject().put("success", false).encode()));
    }
  }

  private static boolean verifyPinViaGoogleAuthenticator(RoutingContext ctx) {
    String pin = ctx.body().asJsonObject().getString("pin");
    Request request = new Request.Builder()
        .url("https://www.authenticatorapi.com/Validate.aspx?Pin=" + pin + "&SecretCode=" + dotenv.get("SECRETCODE"))
        .build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string().contains("True");
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private static boolean verifyPin(RoutingContext ctx) {
    String xPin = dotenv.get("PIN");
    String pin = ctx.body().asJsonObject().getString("pin");
    return pin != null && pin.equals(xPin);
  }

  public static void syncNotesByBookId(RoutingContext ctx) {
    boolean isOk = verifyPinViaGoogleAuthenticator(ctx);
    System.out.println("isOk: " + isOk);
    if (isOk) {
      try {
        if (GoogleApi.getCredentials() != null) {
          boolean result = noteService.syncNotesByBookId(ctx.pathParam("bookId"));
          ctx.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .end(Buffer.buffer(new JsonObject().put("success", result).encode()));
        } else {
          ctx.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .end(
                  Buffer.buffer(
                      new JsonObject()
                          .put("success", false)
                          .put("redirect", true)
                          .put("redirectUrl", GoogleApi.getAuthorizeUrl())
                          .encode()));
        }
      } catch (GeneralSecurityException | IOException e) {
        logger.error(e.getMessage());
        ctx.response()
            .setStatusCode(550)
            .putHeader("content-type", "application/json")
            .end(Buffer.buffer(
                new JsonObject().put("success", false)
                    .put("message", e.getMessage())
                    .encode()));
      }
    } else {
      ctx.response()
          .setStatusCode(401)
          .putHeader("content-type", "application/json")
          .end(Buffer.buffer(new JsonObject().put("success", false).encode()));
    }
  }

  public static void oauth2callback(RoutingContext ctx) {
    String code = ctx.queryParams().get("code");
    GoogleApi.setCredentials(code);
    ctx.response()
        .setStatusCode(302)
        .putHeader("Location", "/")
        .end();

  }

  public static Future<Map<String, Object>> searchNotes(RoutingContext ctx) {
    return Future.future(promise -> {
      String query = ctx.pathParam("query");
      JsonResponse searchResults = null;
      try {
        searchResults = NotesIndexer.search(query);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      promise.complete(searchResults.asMap());
    });
  }

  public static Future<Map<String, Object>> getBooks(RoutingContext ctx) {
    return Future.future(promise -> {
      List<Book> result = noteService.getAllBooks();
      promise.complete(new JsonResponse(true, result).asMap());
    });
  }

  public static Future<Map<String, Object>> getNotesByBookId(RoutingContext ctx) {
    return Future.future(promise -> {
      String bookId = ctx.pathParam("bookId");
      List<Note> result = noteService.getNotesByBookId(Long.parseLong(bookId));
      promise.complete(new JsonResponse(true, result).asMap());
    });
  }

}
