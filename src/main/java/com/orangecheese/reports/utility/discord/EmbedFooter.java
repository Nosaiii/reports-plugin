package com.orangecheese.reports.utility.discord;

public class EmbedFooter {
    private final String text;

    private final String iconUrl;

    public EmbedFooter(String text, String iconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
    }

    public String getText() {
        return text;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
