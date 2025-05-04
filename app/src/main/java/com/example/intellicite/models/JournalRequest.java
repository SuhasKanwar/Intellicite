package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Journal Request model
public class JournalRequest {
    @SerializedName("facultyName")
    private String facultyName;

    @SerializedName("journalTitle")
    private String journalTitle;

    @SerializedName("journalName")
    private String journalName;

    @SerializedName("authorList")
    private List<String> authorList;

    @SerializedName("authorship")
    private String authorship;

    @SerializedName("doi")
    private String doi;

    @SerializedName("datePublished")
    private String datePublished;

    @SerializedName("journalQuartile")
    private String journalQuartile;

    @SerializedName("scopusIndexed")
    private boolean scopusIndexed;

    // Constructor
    public JournalRequest(String facultyName, String journalTitle, String journalName,
                          List<String> authorList, String authorship, String doi,
                          String datePublished, String journalQuartile, boolean scopusIndexed) {
        this.facultyName = facultyName;
        this.journalTitle = journalTitle;
        this.journalName = journalName;
        this.authorList = authorList;
        this.authorship = authorship;
        this.doi = doi;
        this.datePublished = datePublished;
        this.journalQuartile = journalQuartile;
        this.scopusIndexed = scopusIndexed;
    }

    // Getters and Setters
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public List<String> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<String> authorList) {
        this.authorList = authorList;
    }

    public String getAuthorship() {
        return authorship;
    }

    public void setAuthorship(String authorship) {
        this.authorship = authorship;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getJournalQuartile() {
        return journalQuartile;
    }

    public void setJournalQuartile(String journalQuartile) {
        this.journalQuartile = journalQuartile;
    }

    public boolean isScopusIndexed() {
        return scopusIndexed;
    }

    public void setScopusIndexed(boolean scopusIndexed) {
        this.scopusIndexed = scopusIndexed;
    }
}

