package com.filavents.books_highlights.controllers;

import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.services.impl.NoteServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class NoteController {

  private static final NoteService noteService = new NoteServiceImpl();

  private NoteController() {

  }

  public static Future<JsonObject> syncBooks(RoutingContext ctx) {
    return Future.future(promise -> {
      boolean isSucceed = false;
      try {
        isSucceed = noteService.syncBooks();
        promise.complete(new JsonObject().put("isSucceed", isSucceed));
      } catch (GeneralSecurityException | IOException e) {
        promise.fail(e);
      }
    });
  }

}
