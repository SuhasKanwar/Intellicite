package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookChapterRequest {
    @SerializedName("facultyName")
    private String facultyName;

    @SerializedName("chapterTitle")
    private String chapterTitle;

    @SerializedName("bookTitle")
    private String bookTitle;

    @SerializedName("authors")
    private List<String> authors;

    @SerializedName("authorship")
    private String authorship;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("publicationDate")
    private String publicationDate;

    @SerializedName("doi")
    private String doi;

    // Constructor
    public BookChapterRequest(String facultyName, String chapterTitle, String bookTitle,
                              List<String> authors, String authorship, String publisher,
                              String publicationDate, String doi) {
        this.facultyName = facultyName;
        this.chapterTitle = chapterTitle;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.authorship = authorship;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.doi = doi;
    }

    // Getters and setters
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getAuthorship() {
        return authorship;
    }

    public void setAuthorship(String authorship) {
        this.authorship = authorship;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }
}