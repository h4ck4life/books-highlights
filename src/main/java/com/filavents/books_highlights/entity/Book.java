package com.filavents.books_highlights.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "google_book_id", nullable = false)
  private String googleBookId;

  @Column(name = "google_book_title", nullable = false)
  private String googleBookTitle;

  @Column(name = "google_book_author", nullable = false)
  private String googleBookAuthor;

  @Column(name = "google_book_notes_count", nullable = false)
  private String googleBookNotesCount;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
  private List<Note> notes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGoogleBookId() {
    return googleBookId;
  }

  public void setGoogleBookId(String googleBookId) {
    this.googleBookId = googleBookId;
  }

  public String getGoogleBookTitle() {
    return googleBookTitle;
  }

  public void setGoogleBookTitle(String googleBookTitle) {
    this.googleBookTitle = googleBookTitle;
  }

  public String getGoogleBookAuthor() {
    return googleBookAuthor;
  }

  public void setGoogleBookAuthor(String googleBookAuthor) {
    this.googleBookAuthor = googleBookAuthor;
  }

  public String getGoogleBookNotesCount() {
    return googleBookNotesCount;
  }

  public void setGoogleBookNotesCount(String googleBookNotesCount) {
    this.googleBookNotesCount = googleBookNotesCount;
  }

  public List<Note> getNotes() {
    if(notes == null) {
      notes = new ArrayList<>();
    }
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }
}
