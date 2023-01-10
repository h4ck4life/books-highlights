package com.filavents.books_highlights.controllers;

import com.filavents.books_highlights.dto.JsonResponse;
import com.filavents.books_highlights.entity.Book;
import com.filavents.books_highlights.entity.Note;
import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.services.impl.NoteServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
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

  public static Future<Void> syncBooks(RoutingContext ctx) {
    return Future.future(promise -> {
      try {
        boolean result = noteService.syncBooks();
        ctx.response()
          .setStatusCode(200)
          .putHeader("content-type", "application/json")
          .end(Buffer.buffer(new JsonObject().put("success", result).encode()));
        promise.complete();
      } catch (GeneralSecurityException | IOException e) {
        ctx.response()
          .setStatusCode(550)
          .putHeader("content-type", "application/json")
          .end(Buffer.buffer(new JsonObject().put("success", false).encode()));
        promise.fail(e);
      }
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
