package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Journal Response model
public class JournalResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private JournalData data;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public JournalData getData() {
        return data;
    }

    // Journal Data class
    public static class JournalData {
        @SerializedName("id")
        private String id;

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

        // Getters
        public String getId() {
            return id;
        }

        public String getFacultyName() {
            return facultyName;
        }

        public String getJournalTitle() {
            return journalTitle;
        }

        public String getJournalName() {
            return journalName;
        }

        public List<String> getAuthorList() {
            return authorList;
        }

        public String getAuthorship() {
            return authorship;
        }

        public String getDoi() {
            return doi;
        }

        public String getDatePublished() {
            return datePublished;
        }

        public String getJournalQuartile() {
            return journalQuartile;
        }

        public boolean isScopusIndexed() {
            return scopusIndexed;
        }
    }
}