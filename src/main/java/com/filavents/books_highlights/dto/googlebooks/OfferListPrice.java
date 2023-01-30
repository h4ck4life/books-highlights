package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class OfferListPrice {
    private long amountInMicros;
    private String currencyCode;

    @JsonProperty("amountInMicros")
    public long getAmountInMicros() { return amountInMicros; }
    @JsonProperty("amountInMicros")
    public void setAmountInMicros(long value) { this.amountInMicros = value; }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() { return currencyCode; }
    @JsonProperty("currencyCode")
    public void setCurrencyCode(String value) { this.currencyCode = value; }
}
