package uga.menik.csx370.models;

import java.sql.Date;

/**
 * Represents a job posting made by a company.
 */
public class JobPosting {
    private final int jobId;
    private final int companyId;
    private final String companyName;
    private final String title;
    private final String location;
    private final String employmentType;
    private final String jobLevel;
    private final Date postingDate;
    private final Date applicationDeadline;
    private final String season;
    private final boolean isOpen;

    public JobPosting(int jobId, int companyId, String companyName, String title, String location,
                      String employmentType, String jobLevel, Date postingDate, Date applicationDeadline,
                      String season, boolean isOpen) {
        this.jobId = jobId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.title = title;
        this.location = location;
        this.employmentType = employmentType;
        this.jobLevel = jobLevel;
        this.postingDate = postingDate;
        this.applicationDeadline = applicationDeadline;
        this.season = season;
        this.isOpen = isOpen;
    }

    public int getJobId() {
        return jobId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTitle() {
        return title;
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

    public Date getPostingDate() {
        return postingDate;
    }

    public Date getApplicationDeadline() {
        return applicationDeadline;
    }

    public String getSeason() {
        return season;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
