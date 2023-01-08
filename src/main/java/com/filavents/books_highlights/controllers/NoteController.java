package com.filavents.books_highlights.controllers;

import com.filavents.books_highlights.services.NoteService;
import com.filavents.books_highlights.services.impl.NoteServiceImpl;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class NoteController {

  private static final NoteService noteService = new NoteServiceImpl();

  private NoteController() {

  }

  public static Future<Map<String, Object>> syncBooks(RoutingContext ctx) {
    return Future.future(promise -> {
        noteService.getAllBooks();
    });
  }

}
