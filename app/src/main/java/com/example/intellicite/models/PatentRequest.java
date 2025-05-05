package com.example.intellicite.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatentRequest {
    @SerializedName("patentTitle")
    private String patentTitle;

    @SerializedName("filledBy")
    private String filledBy;

    @SerializedName("patentType")
    private String patentType;

    @SerializedName("inventors")
    private List<String> inventors;

    @SerializedName("patentFilingAgency")
    private String patentFilingAgency;

    @SerializedName("patentStatus")
    private String patentStatus;

    @SerializedName("dateOfPatent")
    private String dateOfPatent;

    @SerializedName("patentNumber")
    private String patentNumber;

    @SerializedName("patentUrl")
    private String patentUrl;

    @SerializedName("supportingDocuments")
    private List<String> supportingDocuments;

    // Constructor
    public PatentRequest(String patentTitle, String filledBy, String patentType,
                         List<String> inventors, String patentFilingAgency,
                         String patentStatus, String dateOfPatent, String patentNumber,
                         String patentUrl, List<String> supportingDocuments) {
        this.patentTitle = patentTitle;
        this.filledBy = filledBy;
        this.patentType = patentType;
        this.inventors = inventors;
        this.patentFilingAgency = patentFilingAgency;
        this.patentStatus = patentStatus;
        this.dateOfPatent = dateOfPatent;
        this.patentNumber = patentNumber;
        this.patentUrl = patentUrl;
        this.supportingDocuments = supportingDocuments;
    }

    // Getters and Setters
    public String getPatentTitle() {
        return patentTitle;
    }

    public void setPatentTitle(String patentTitle) {
        this.patentTitle = patentTitle;
    }

    public String getFilledBy() {
        return filledBy;
    }

    public void setFilledBy(String filledBy) {
        this.filledBy = filledBy;
    }

    public String getPatentType() {
        return patentType;
    }

    public void setPatentType(String patentType) {
        this.patentType = patentType;
    }

    public List<String> getInventors() {
        return inventors;
    }

    public void setInventors(List<String> inventors) {
        this.inventors = inventors;
    }

    public String getPatentFilingAgency() {
        return patentFilingAgency;
    }

    public void setPatentFilingAgency(String patentFilingAgency) {
        this.patentFilingAgency = patentFilingAgency;
    }

    public String getPatentStatus() {
        return patentStatus;
    }

    public void setPatentStatus(String patentStatus) {
        this.patentStatus = patentStatus;
    }

    public String getDateOfPatent() {
        return dateOfPatent;
    }

    public void setDateOfPatent(String dateOfPatent) {
        this.dateOfPatent = dateOfPatent;
    }

    public String getPatentNumber() {
        return patentNumber;
    }

    public void setPatentNumber(String patentNumber) {
        this.patentNumber = patentNumber;
    }

    public String getPatentUrl() {
        return patentUrl;
    }

    public void setPatentUrl(String patentUrl) {
        this.patentUrl = patentUrl;
    }

    public List<String> getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(List<String> supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }
}