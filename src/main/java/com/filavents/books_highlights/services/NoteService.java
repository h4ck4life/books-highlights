package com.filavents.books_highlights.services;

import com.filavents.books_highlights.entity.Book;

import java.util.List;

public interface NoteService {
  boolean syncBooks();
  List<Book> getAllBooks();
  Book getBookById(Long id);

}
