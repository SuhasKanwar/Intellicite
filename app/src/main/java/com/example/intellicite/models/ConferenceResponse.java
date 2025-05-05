package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConferenceResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ConferenceData data;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ConferenceData getData() {
        return data;
    }

    // Conference Data class
    public static class ConferenceData {
        @SerializedName("id")
        private String id;

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

        // Getters
        public String getId() {
            return id;
        }

        public String getFacultyName() {
            return facultyName;
        }

        public String getConferencePaperTitle() {
            return conferencePaperTitle;
        }

        public List<String> getAuthorList() {
            return authorList;
        }

        public String getAuthorship() {
            return authorship;
        }

        public String getConferenceName() {
            return conferenceName;
        }

        public String getConferenceLocation() {
            return conferenceLocation;
        }

        public String getConferenceDate() {
            return conferenceDate;
        }

        public String getDoi() {
            return doi;
        }

        public String getDateOfPublication() {
            return dateOfPublication;
        }

        public String getConferenceIndexed() {
            return conferenceIndexed;
        }
    }
}