package com.filavents.books_highlights.services;

import com.filavents.books_highlights.entity.Book;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface NoteService {
  boolean syncBooks() throws GeneralSecurityException, IOException;
  List<Book> getAllBooks();
  Book getBookById(Long id);

}
