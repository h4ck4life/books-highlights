package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class Item {
    private String kind;
    private String id;
    private String etag;
    private String selfLink;
    private VolumeInfo volumeInfo;
    private SaleInfo saleInfo;
    private AccessInfo accessInfo;
    private SearchInfo searchInfo;

    @JsonProperty("kind")
    public String getKind() { return kind; }
    @JsonProperty("kind")
    public void setKind(String value) { this.kind = value; }

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("etag")
    public String getEtag() { return etag; }
    @JsonProperty("etag")
    public void setEtag(String value) { this.etag = value; }

    @JsonProperty("selfLink")
    public String getSelfLink() { return selfLink; }
    @JsonProperty("selfLink")
    public void setSelfLink(String value) { this.selfLink = value; }

    @JsonProperty("volumeInfo")
    public VolumeInfo getVolumeInfo() { return volumeInfo; }
    @JsonProperty("volumeInfo")
    public void setVolumeInfo(VolumeInfo value) { this.volumeInfo = value; }

    @JsonProperty("saleInfo")
    public SaleInfo getSaleInfo() { return saleInfo; }
    @JsonProperty("saleInfo")
    public void setSaleInfo(SaleInfo value) { this.saleInfo = value; }

    @JsonProperty("accessInfo")
    public AccessInfo getAccessInfo() { return accessInfo; }
    @JsonProperty("accessInfo")
    public void setAccessInfo(AccessInfo value) { this.accessInfo = value; }

    @JsonProperty("searchInfo")
    public SearchInfo getSearchInfo() { return searchInfo; }
    @JsonProperty("searchInfo")
    public void setSearchInfo(SearchInfo value) { this.searchInfo = value; }
}
