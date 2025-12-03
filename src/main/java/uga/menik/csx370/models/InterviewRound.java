package uga.menik.csx370.models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Represents a single interview interaction for an application.
 */
public class InterviewRound {
    private final int interviewId;
    private final int applicationId;
    private final String roundType;
    private final Date scheduledDate;
    private final String status;
    private final String feedback;
    private final Timestamp createdAt;

    public InterviewRound(int interviewId, int applicationId, String roundType, Date scheduledDate,
                          String status, String feedback, Timestamp createdAt) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.roundType = roundType;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public int getInterviewId() {
        return interviewId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getRoundType() {
        return roundType;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public String getStatus() {
        return status;
    }

    public String getFeedback() {
        return feedback;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
