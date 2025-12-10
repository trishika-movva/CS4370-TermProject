package uga.menik.csx370.models;

import java.sql.Date;

/**
 * Represents an interview round in a list view.
 */
public class InterviewItem {
    private final int interviewId;
    private final int applicationId;
    private final String companyName;
    private final String jobTitle;
    private final String roundType;
    private final String status;
    private final Date scheduledDate;

    public InterviewItem(int interviewId, int applicationId, String companyName, String jobTitle,
                         String roundType, String status, Date scheduledDate) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.roundType = roundType;
        this.status = status;
        this.scheduledDate = scheduledDate;
    }

    public int getInterviewId() {
        return interviewId;
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

    public String getRoundType() {
        return roundType;
    }

    public String getStatus() {
        return status;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }
}
