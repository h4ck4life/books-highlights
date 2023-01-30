package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class Offer {
    private long finskyOfferType;
    private OfferListPrice listPrice;
    private OfferListPrice retailPrice;

    @JsonProperty("finskyOfferType")
    public long getFinskyOfferType() { return finskyOfferType; }
    @JsonProperty("finskyOfferType")
    public void setFinskyOfferType(long value) { this.finskyOfferType = value; }

    @JsonProperty("listPrice")
    public OfferListPrice getListPrice() { return listPrice; }
    @JsonProperty("listPrice")
    public void setListPrice(OfferListPrice value) { this.listPrice = value; }

    @JsonProperty("retailPrice")
    public OfferListPrice getRetailPrice() { return retailPrice; }
    @JsonProperty("retailPrice")
    public void setRetailPrice(OfferListPrice value) { this.retailPrice = value; }
}
