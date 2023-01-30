package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class Epub {
    private boolean isAvailable;
    private String acsTokenLink;

    @JsonProperty("isAvailable")
    public boolean getIsAvailable() { return isAvailable; }
    @JsonProperty("isAvailable")
    public void setIsAvailable(boolean value) { this.isAvailable = value; }

    @JsonProperty("acsTokenLink")
    public String getAcsTokenLink() { return acsTokenLink; }
    @JsonProperty("acsTokenLink")
    public void setAcsTokenLink(String value) { this.acsTokenLink = value; }
}
