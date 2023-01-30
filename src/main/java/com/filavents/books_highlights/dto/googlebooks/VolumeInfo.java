package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

public class VolumeInfo {
    private String title;
    private String subtitle;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifier> industryIdentifiers;
    private ReadingModes readingModes;
    private long pageCount;
    private String printType;
    private List<String> categories;
    private Double averageRating;
    private Long ratingsCount;
    private String maturityRating;
    private boolean allowAnonLogging;
    private String contentVersion;
    private PanelizationSummary panelizationSummary;
    private ImageLinks imageLinks;
    private String language;
    private String previewLink;
    private String infoLink;
    private String canonicalVolumeLink;

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("subtitle")
    public String getSubtitle() { return subtitle; }
    @JsonProperty("subtitle")
    public void setSubtitle(String value) { this.subtitle = value; }

    @JsonProperty("authors")
    public List<String> getAuthors() { return authors; }
    @JsonProperty("authors")
    public void setAuthors(List<String> value) { this.authors = value; }

    @JsonProperty("publisher")
    public String getPublisher() { return publisher; }
    @JsonProperty("publisher")
    public void setPublisher(String value) { this.publisher = value; }

    @JsonProperty("publishedDate")
    public String getPublishedDate() { return publishedDate; }
    @JsonProperty("publishedDate")
    public void setPublishedDate(String value) { this.publishedDate = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("industryIdentifiers")
    public List<IndustryIdentifier> getIndustryIdentifiers() { return industryIdentifiers; }
    @JsonProperty("industryIdentifiers")
    public void setIndustryIdentifiers(List<IndustryIdentifier> value) { this.industryIdentifiers = value; }

    @JsonProperty("readingModes")
    public ReadingModes getReadingModes() { return readingModes; }
    @JsonProperty("readingModes")
    public void setReadingModes(ReadingModes value) { this.readingModes = value; }

    @JsonProperty("pageCount")
    public long getPageCount() { return pageCount; }
    @JsonProperty("pageCount")
    public void setPageCount(long value) { this.pageCount = value; }

    @JsonProperty("printType")
    public String getPrintType() { return printType; }
    @JsonProperty("printType")
    public void setPrintType(String value) { this.printType = value; }

    @JsonProperty("categories")
    public List<String> getCategories() { return categories; }
    @JsonProperty("categories")
    public void setCategories(List<String> value) { this.categories = value; }

    @JsonProperty("averageRating")
    public Double getAverageRating() { return averageRating; }
    @JsonProperty("averageRating")
    public void setAverageRating(Double value) { this.averageRating = value; }

    @JsonProperty("ratingsCount")
    public Long getRatingsCount() { return ratingsCount; }
    @JsonProperty("ratingsCount")
    public void setRatingsCount(Long value) { this.ratingsCount = value; }

    @JsonProperty("maturityRating")
    public String getMaturityRating() { return maturityRating; }
    @JsonProperty("maturityRating")
    public void setMaturityRating(String value) { this.maturityRating = value; }

    @JsonProperty("allowAnonLogging")
    public boolean getAllowAnonLogging() { return allowAnonLogging; }
    @JsonProperty("allowAnonLogging")
    public void setAllowAnonLogging(boolean value) { this.allowAnonLogging = value; }

    @JsonProperty("contentVersion")
    public String getContentVersion() { return contentVersion; }
    @JsonProperty("contentVersion")
    public void setContentVersion(String value) { this.contentVersion = value; }

    @JsonProperty("panelizationSummary")
    public PanelizationSummary getPanelizationSummary() { return panelizationSummary; }
    @JsonProperty("panelizationSummary")
    public void setPanelizationSummary(PanelizationSummary value) { this.panelizationSummary = value; }

    @JsonProperty("imageLinks")
    public ImageLinks getImageLinks() { return imageLinks; }
    @JsonProperty("imageLinks")
    public void setImageLinks(ImageLinks value) { this.imageLinks = value; }

    @JsonProperty("language")
    public String getLanguage() { return language; }
    @JsonProperty("language")
    public void setLanguage(String value) { this.language = value; }

    @JsonProperty("previewLink")
    public String getPreviewLink() { return previewLink; }
    @JsonProperty("previewLink")
    public void setPreviewLink(String value) { this.previewLink = value; }

    @JsonProperty("infoLink")
    public String getInfoLink() { return infoLink; }
    @JsonProperty("infoLink")
    public void setInfoLink(String value) { this.infoLink = value; }

    @JsonProperty("canonicalVolumeLink")
    public String getCanonicalVolumeLink() { return canonicalVolumeLink; }
    @JsonProperty("canonicalVolumeLink")
    public void setCanonicalVolumeLink(String value) { this.canonicalVolumeLink = value; }
}
