package uga.menik.csx370.models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * View model for application list/detail pages with joined job + company fields.
 */
public class ApplicationView {
    private final int applicationId;
    private final String companyName;
    private final String jobTitle;
    private final String location;
    private final String employmentType;
    private final String jobLevel;
    private final String status;
    private final Date appliedDate;
    private final String season;
    private final String source;
    private final String resumeVersion;
    private final String notes;
    private final Timestamp lastUpdated;

    public ApplicationView(int applicationId, String companyName, String jobTitle, String location,
                           String employmentType, String jobLevel, String status, Date appliedDate,
                           String season, String source, String resumeVersion, String notes, Timestamp lastUpdated) {
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.employmentType = employmentType;
        this.jobLevel = jobLevel;
        this.status = status;
        this.appliedDate = appliedDate;
        this.season = season;
        this.source = source;
        this.resumeVersion = resumeVersion;
        this.notes = notes;
        this.lastUpdated = lastUpdated;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public String getStatus() {
        return status;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public String getSeason() {
        return season;
    }

    public String getSource() {
        return source;
    }

    public String getResumeVersion() {
        return resumeVersion;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }
}
