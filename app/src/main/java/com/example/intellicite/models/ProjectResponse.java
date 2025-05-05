package com.example.intellicite.models;

public class ProjectResponse {
    private boolean success;
    private String message;
    private ProjectData data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProjectData getData() {
        return data;
    }

    public void setData(ProjectData data) {
        this.data = data;
    }

    public static class ProjectData {
        private String id;
        private String facultyName;
        private String projectTitle;
        private String fundingAgency;
        private String projectType;
        private double amount;
        private String status;
        private String dateOfProposalOrGranted;
        private String[] supportingDocumentsUrl;
        private String period;
        private String createdAt;
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}