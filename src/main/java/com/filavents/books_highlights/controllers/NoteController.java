package com.filavents.books_highlights.controllers;

import com.filavents.books_highlights.dto.JsonResponse;
import com.filavents.books_highlights.entity.Book;
import com.filavents.books_highlights.entity.Note;
import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.services.impl.NoteServiceImpl;
import com.filavents.books_highlights.utils.GoogleApi;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public class NoteController {

  private static final NoteService noteService = new NoteServiceImpl();

  private NoteController() {

  }

  static Logger logger = LoggerFactory.getLogger(NoteController.class);

  public static void syncBooks(RoutingContext ctx) {
    boolean isOk = verifyPin(ctx);
    if (isOk) {
      try {
        boolean result = noteService.syncBooks();
        ctx.response()
          .setStatusCode(200)
          .putHeader("content-type", "application/json")
          .end(Buffer.buffer(new JsonObject().put("success", result).encode()));
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

  private static boolean verifyPin(RoutingContext ctx) {
    String xPin = System.getenv("PIN");
    String pin = ctx.body().asJsonObject().getString("pin");
    return pin != null && pin.equals(xPin);
  }

  public static void syncNotesByBookId(RoutingContext ctx) {
    boolean isOk = verifyPin(ctx);
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
                  .encode()
              )
            );
        }
      } catch (GeneralSecurityException | IOException e) {
        logger.error(e.getMessage());
        ctx.response()
          .setStatusCode(550)
          .putHeader("content-type", "application/json")
          .end(Buffer.buffer(
              new JsonObject().put("success", false)
                .put("message", e.getMessage())
                .encode()
            )
          );
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
