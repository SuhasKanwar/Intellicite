package com.example.intellicite.models;

public class ProjectRequest {
    private String facultyName;
    private String projectTitle;
    private String fundingAgency;
    private String projectType;
    private double amount;
    private String status;
    private String dateOfProposalOrGranted;
    private String[] supportingDocumentsUrl;
    private String period;

    public ProjectRequest(String facultyName, String projectTitle, String fundingAgency,
                          String projectType, double amount, String status,
                          String dateOfProposalOrGranted, String[] supportingDocumentsUrl,
                          String period) {
        this.facultyName = facultyName;
        this.projectTitle = projectTitle;
        this.fundingAgency = fundingAgency;
        this.projectType = projectType;
        this.amount = amount;
        this.status = status;
        this.dateOfProposalOrGranted = dateOfProposalOrGranted;
        this.supportingDocumentsUrl = supportingDocumentsUrl;
        this.period = period;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getFundingAgency() {
        return fundingAgency;
    }

    public void setFundingAgency(String fundingAgency) {
        this.fundingAgency = fundingAgency;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateOfProposalOrGranted() {
        return dateOfProposalOrGranted;
    }

    public void setDateOfProposalOrGranted(String dateOfProposalOrGranted) {
        this.dateOfProposalOrGranted = dateOfProposalOrGranted;
    }

    public String[] getSupportingDocumentsUrl() {
        return supportingDocumentsUrl;
    }

    public void setSupportingDocumentsUrl(String[] supportingDocumentsUrl) {
        this.supportingDocumentsUrl = supportingDocumentsUrl;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}