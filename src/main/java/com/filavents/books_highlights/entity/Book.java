package com.filavents.books_highlights.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Column(name = "drive_id", nullable = false)
  private String driveId;

  @Column(name = "book_title", nullable = false)
  private String bookTitle;

  @Column(name = "book_author", nullable = false)
  private String bookAuthor;

  @Column(name = "book_notes_count", nullable = false)
  private String bookNotesCount;

  @Column(name = "drive_mimeType", nullable = false)
  private String driveMimeType;

  @Column(name = "drive_modifiedTime", nullable = false)
  private long driveModifiedTime;

  @Column(name = "drive_createdTime", nullable = false)
  private long driveCreatedTime;

  @Column(name = "drive_file_name", nullable = false)
  private String driveFileName;

  @Column(name = "drive_file_size", nullable = false)
  private Long driveFileSize;

  @Column(name = "drive_file_extension")
  private String driveFileExtension;

  @Column(name = "drive_webViewLink", nullable = false)
  private String driveWebViewLink;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Note> notes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDriveId() {
    return driveId;
  }

  public void setDriveId(String driveId) {
    this.driveId = driveId;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getBookAuthor() {
    return bookAuthor;
  }

  public void setBookAuthor(String bookAuthor) {
    this.bookAuthor = bookAuthor;
  }

  public String getBookNotesCount() {
    return bookNotesCount;
  }

  public void setBookNotesCount(String bookNotesCount) {
    this.bookNotesCount = bookNotesCount;
  }

  public String getDriveMimeType() {
    return driveMimeType;
  }

  public void setDriveMimeType(String driveMimeType) {
    this.driveMimeType = driveMimeType;
  }

  public long getDriveModifiedTime() {
    return driveModifiedTime;
  }

  public void setDriveModifiedTime(long driveModifiedTime) {
    this.driveModifiedTime = driveModifiedTime;
  }

  public long getDriveCreatedTime() {
    return driveCreatedTime;
  }

  public void setDriveCreatedTime(long driveCreatedTime) {
    this.driveCreatedTime = driveCreatedTime;
  }

  public String getDriveFileName() {
    return driveFileName;
  }

  public void setDriveFileName(String driveFileName) {
    this.driveFileName = driveFileName;
  }

  public Long getDriveFileSize() {
    return driveFileSize;
  }

  public void setDriveFileSize(Long driveFileSize) {
    this.driveFileSize = driveFileSize;
  }

  public String getDriveFileExtension() {
    return driveFileExtension;
  }

  public void setDriveFileExtension(String driveFileExtension) {
    this.driveFileExtension = driveFileExtension;
  }

  public String getDriveWebViewLink() {
    return driveWebViewLink;
  }

  public void setDriveWebViewLink(String driveWebViewLink) {
    this.driveWebViewLink = driveWebViewLink;
  }

  public List<Note> getNotes() {
    if (notes == null) {
      notes = new ArrayList<>();
    }
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "Book{" +
      "id=" + id +
      ", driveId='" + driveId + '\'' +
      ", bookTitle='" + bookTitle + '\'' +
      ", bookAuthor='" + bookAuthor + '\'' +
      ", bookNotesCount='" + bookNotesCount + '\'' +
      ", driveMimeType='" + driveMimeType + '\'' +
      ", driveModifiedTime=" + driveModifiedTime +
      ", driveCreatedTime=" + driveCreatedTime +
      ", driveFileName='" + driveFileName + '\'' +
      ", driveFileSize=" + driveFileSize +
      ", driveFileExtension='" + driveFileExtension + '\'' +
      ", driveWebViewLink='" + driveWebViewLink + '\'' +
      ", notes=" + notes +
      '}';
  }
}
