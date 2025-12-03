package uga.menik.csx370.models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Represents an application submitted by a user.
 */
public class Application {
    private final int applicationId;
    private final int userId;
    private final int jobId;
    private final Date appliedDate;
    private final String status;
    private final String source;
    private final String resumeVersion;
    private final String notes;
    private final Timestamp lastUpdated;

    public Application(int applicationId, int userId, int jobId, Date appliedDate, String status,
                       String source, String resumeVersion, String notes, Timestamp lastUpdated) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.jobId = jobId;
        this.appliedDate = appliedDate;
        this.status = status;
        this.source = source;
        this.resumeVersion = resumeVersion;
        this.notes = notes;
        this.lastUpdated = lastUpdated;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getJobId() {
        return jobId;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public String getStatus() {
        return status;
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
