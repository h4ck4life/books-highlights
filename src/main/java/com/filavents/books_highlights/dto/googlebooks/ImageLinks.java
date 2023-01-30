package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;

    @JsonProperty("smallThumbnail")
    public String getSmallThumbnail() { return smallThumbnail; }
    @JsonProperty("smallThumbnail")
    public void setSmallThumbnail(String value) { this.smallThumbnail = value; }

    @JsonProperty("thumbnail")
    public String getThumbnail() { return thumbnail; }
    @JsonProperty("thumbnail")
    public void setThumbnail(String value) { this.thumbnail = value; }
}
