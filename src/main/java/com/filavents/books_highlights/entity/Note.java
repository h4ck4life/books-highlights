package com.filavents.books_highlights.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "notes")
public class Note {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "google_book_note", nullable = false)
  private String getGoogleBookNote;

  @Column(name = "google_book_id", nullable = false)
  private String googleBookId;

  @Column(name = "google_book_date", nullable = false)
  private String googleBookDate;

  @Column(name = "google_book_url", nullable = false)
  private String noteUrl;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGetGoogleBookNote() {
    return getGoogleBookNote;
  }

  public void setGetGoogleBookNote(String getGoogleBookNote) {
    this.getGoogleBookNote = getGoogleBookNote;
  }

  public String getGoogleBookId() {
    return googleBookId;
  }

  public void setGoogleBookId(String googleBookId) {
    this.googleBookId = googleBookId;
  }

  public String getGoogleBookDate() {
    return googleBookDate;
  }

  public void setGoogleBookDate(String googleBookDate) {
    this.googleBookDate = googleBookDate;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public String getNoteUrl() {
    return noteUrl;
  }

  public void setNoteUrl(String noteUrl) {
    this.noteUrl = noteUrl;
  }
}
