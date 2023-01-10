package com.filavents.books_highlights.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

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

  @Column(name = "google_book_date")
  private LocalDate googleBookDate;

  @Column(name = "google_book_url")
  private String noteUrl;

  @ManyToOne
  @JoinColumn(name = "book_id")
  @JsonIgnore
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

  public LocalDate getGoogleBookDate() {
    return googleBookDate;
  }

  public void setGoogleBookDate(LocalDate googleBookDate) {
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

  @Override
  public String toString() {
    return "Note{" +
      "id=" + id +
      ", getGoogleBookNote='" + getGoogleBookNote + '\'' +
      ", googleBookId='" + googleBookId + '\'' +
      ", googleBookDate=" + googleBookDate +
      ", noteUrl='" + noteUrl + '\'' +
      ", book=" + book +
      '}';
  }
}
