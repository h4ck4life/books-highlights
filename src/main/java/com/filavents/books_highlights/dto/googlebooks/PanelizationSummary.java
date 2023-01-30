package com.filavents.books_highlights.dto.googlebooks;

import com.fasterxml.jackson.annotation.*;

public class PanelizationSummary {
    private boolean containsEpubBubbles;
    private boolean containsImageBubbles;

    @JsonProperty("containsEpubBubbles")
    public boolean getContainsEpubBubbles() { return containsEpubBubbles; }
    @JsonProperty("containsEpubBubbles")
    public void setContainsEpubBubbles(boolean value) { this.containsEpubBubbles = value; }

    @JsonProperty("containsImageBubbles")
    public boolean getContainsImageBubbles() { return containsImageBubbles; }
    @JsonProperty("containsImageBubbles")
    public void setContainsImageBubbles(boolean value) { this.containsImageBubbles = value; }
}
