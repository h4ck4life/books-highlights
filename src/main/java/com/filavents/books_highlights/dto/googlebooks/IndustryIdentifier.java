package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class IndustryIdentifier {
    private String type;
    private String identifier;

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("identifier")
    public String getIdentifier() { return identifier; }
    @JsonProperty("identifier")
    public void setIdentifier(String value) { this.identifier = value; }
}
