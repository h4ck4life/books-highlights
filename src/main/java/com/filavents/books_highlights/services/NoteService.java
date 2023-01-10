package com.filavents.books_highlights.services;

import com.filavents.books_highlights.entity.Book;
import com.filavents.books_highlights.entity.Note;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface NoteService {
  boolean syncBooks() throws GeneralSecurityException, IOException;
  List<Book> getAllBooks();
  List<Note> getNotesByBookId(Long id);

}
