package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookRequest {
    @SerializedName("type")
    private String type;

    @SerializedName("authors")
    private List<String> authors;

    @SerializedName("title")
    private String title;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("url")
    private String url;

    @SerializedName("doi")
    private String doi;

    @SerializedName("publishedDate")
    private String publishedDate;

    // Constructor
    public BookRequest(String type, List<String> authors, String title, String publisher,
                       String isbn, String url, String doi, String publishedDate) {
        this.type = type;
        this.authors = authors;
        this.title = title;
        this.publisher = publisher;
        this.isbn = isbn;
        this.url = url;
        this.doi = doi;
        this.publishedDate = publishedDate;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}