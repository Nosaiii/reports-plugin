package com.orangecheese.reports.utility.discord;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmbedObject {
    private String title;
    private String description;
    private String url;
    private Color color;

    private EmbedFooter footer;
    private EmbedThumbnail thumbnail;
    private EmbedImage image;
    private EmbedAuthor author;
    private final List<DiscordField> fields = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Color getColor() {
        return color;
    }

    public EmbedFooter getFooter() {
        return footer;
    }

    public EmbedThumbnail getThumbnail() {
        return thumbnail;
    }

    public EmbedImage getImage() {
        return image;
    }

    public EmbedAuthor getAuthor() {
        return author;
    }

    public List<DiscordField> getFields() {
        return fields;
    }

    public EmbedObject setTitle(String title) {
        this.title = title;
        return this;
    }

    public EmbedObject setDescription(String description) {
        this.description = description;
        return this;
    }

    public EmbedObject setUrl(String url) {
        this.url = url;
        return this;
    }

    public EmbedObject setColor(Color color) {
        this.color = color;
        return this;
    }

    public EmbedObject setFooter(String text, String icon) {
        this.footer = new EmbedFooter(text, icon);
        return this;
    }

    public EmbedObject setThumbnail(String url) {
        this.thumbnail = new EmbedThumbnail(url);
        return this;
    }

    public EmbedObject setImage(String url) {
        this.image = new EmbedImage(url);
        return this;
    }

    public EmbedObject setAuthor(String name, String url, String icon) {
        this.author = new EmbedAuthor(name, url, icon);
        return this;
    }

    public EmbedObject addField(String name, String value, boolean inline) {
        this.fields.add(new DiscordField(name, value, inline));
        return this;
    }
}