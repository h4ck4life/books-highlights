package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class AccessInfo {
    private String country;
    private String viewability;
    private boolean embeddable;
    private boolean publicDomain;
    private String textToSpeechPermission;
    private Epub epub;
    private Epub pdf;
    private String webReaderLink;
    private String accessViewStatus;
    private boolean quoteSharingAllowed;

    @JsonProperty("country")
    public String getCountry() { return country; }
    @JsonProperty("country")
    public void setCountry(String value) { this.country = value; }

    @JsonProperty("viewability")
    public String getViewability() { return viewability; }
    @JsonProperty("viewability")
    public void setViewability(String value) { this.viewability = value; }

    @JsonProperty("embeddable")
    public boolean getEmbeddable() { return embeddable; }
    @JsonProperty("embeddable")
    public void setEmbeddable(boolean value) { this.embeddable = value; }

    @JsonProperty("publicDomain")
    public boolean getPublicDomain() { return publicDomain; }
    @JsonProperty("publicDomain")
    public void setPublicDomain(boolean value) { this.publicDomain = value; }

    @JsonProperty("textToSpeechPermission")
    public String getTextToSpeechPermission() { return textToSpeechPermission; }
    @JsonProperty("textToSpeechPermission")
    public void setTextToSpeechPermission(String value) { this.textToSpeechPermission = value; }

    @JsonProperty("epub")
    public Epub getEpub() { return epub; }
    @JsonProperty("epub")
    public void setEpub(Epub value) { this.epub = value; }

    @JsonProperty("pdf")
    public Epub getPDF() { return pdf; }
    @JsonProperty("pdf")
    public void setPDF(Epub value) { this.pdf = value; }

    @JsonProperty("webReaderLink")
    public String getWebReaderLink() { return webReaderLink; }
    @JsonProperty("webReaderLink")
    public void setWebReaderLink(String value) { this.webReaderLink = value; }

    @JsonProperty("accessViewStatus")
    public String getAccessViewStatus() { return accessViewStatus; }
    @JsonProperty("accessViewStatus")
    public void setAccessViewStatus(String value) { this.accessViewStatus = value; }

    @JsonProperty("quoteSharingAllowed")
    public boolean getQuoteSharingAllowed() { return quoteSharingAllowed; }
    @JsonProperty("quoteSharingAllowed")
    public void setQuoteSharingAllowed(boolean value) { this.quoteSharingAllowed = value; }
}
