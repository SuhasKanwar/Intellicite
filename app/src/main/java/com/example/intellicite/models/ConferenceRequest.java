package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Conference Request model
public class ConferenceRequest {
    @SerializedName("facultyName")
    private String facultyName;

    @SerializedName("conferencePaperTitle")
    private String conferencePaperTitle;

    @SerializedName("authorList")
    private List<String> authorList;

    @SerializedName("authorship")
    private String authorship;

    @SerializedName("conferenceName")
    private String conferenceName;

    @SerializedName("conferenceLocation")
    private String conferenceLocation;

    @SerializedName("conferenceDate")
    private String conferenceDate;

    @SerializedName("doi")
    private String doi;

    @SerializedName("dateOfPublication")
    private String dateOfPublication;

    @SerializedName("conferenceIndexed")
    private String conferenceIndexed;

    // Constructor
    public ConferenceRequest(String facultyName, String conferencePaperTitle, List<String> authorList,
                             String authorship, String conferenceName, String conferenceLocation,
                             String conferenceDate, String doi, String dateOfPublication,
                             String conferenceIndexed) {
        this.facultyName = facultyName;
        this.conferencePaperTitle = conferencePaperTitle;
        this.authorList = authorList;
        this.authorship = authorship;
        this.conferenceName = conferenceName;
        this.conferenceLocation = conferenceLocation;
        this.conferenceDate = conferenceDate;
        this.doi = doi;
        this.dateOfPublication = dateOfPublication;
        this.conferenceIndexed = conferenceIndexed;
    }

    // Getters and Setters
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getConferencePaperTitle() {
        return conferencePaperTitle;
    }

    public void setConferencePaperTitle(String conferencePaperTitle) {
        this.conferencePaperTitle = conferencePaperTitle;
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

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getConferenceLocation() {
        return conferenceLocation;
    }

    public void setConferenceLocation(String conferenceLocation) {
        this.conferenceLocation = conferenceLocation;
    }

    public String getConferenceDate() {
        return conferenceDate;
    }

    public void setConferenceDate(String conferenceDate) {
        this.conferenceDate = conferenceDate;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public String getConferenceIndexed() {
        return conferenceIndexed;
    }

    public void setConferenceIndexed(String conferenceIndexed) {
        this.conferenceIndexed = conferenceIndexed;
    }
}


