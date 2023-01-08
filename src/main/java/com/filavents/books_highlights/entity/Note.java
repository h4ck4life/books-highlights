package com.filavents.books_highlights.entity;

import jakarta.persistence.*;

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

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


}
