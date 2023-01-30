package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

public class SaleInfo {
    private String country;
    private String saleability;
    private boolean isEbook;
    private SaleInfoListPrice listPrice;
    private SaleInfoListPrice retailPrice;
    private String buyLink;
    private List<Offer> offers;

    @JsonProperty("country")
    public String getCountry() { return country; }
    @JsonProperty("country")
    public void setCountry(String value) { this.country = value; }

    @JsonProperty("saleability")
    public String getSaleability() { return saleability; }
    @JsonProperty("saleability")
    public void setSaleability(String value) { this.saleability = value; }

    @JsonProperty("isEbook")
    public boolean getIsEbook() { return isEbook; }
    @JsonProperty("isEbook")
    public void setIsEbook(boolean value) { this.isEbook = value; }

    @JsonProperty("listPrice")
    public SaleInfoListPrice getListPrice() { return listPrice; }
    @JsonProperty("listPrice")
    public void setListPrice(SaleInfoListPrice value) { this.listPrice = value; }

    @JsonProperty("retailPrice")
    public SaleInfoListPrice getRetailPrice() { return retailPrice; }
    @JsonProperty("retailPrice")
    public void setRetailPrice(SaleInfoListPrice value) { this.retailPrice = value; }

    @JsonProperty("buyLink")
    public String getBuyLink() { return buyLink; }
    @JsonProperty("buyLink")
    public void setBuyLink(String value) { this.buyLink = value; }

    @JsonProperty("offers")
    public List<Offer> getOffers() { return offers; }
    @JsonProperty("offers")
    public void setOffers(List<Offer> value) { this.offers = value; }
}
