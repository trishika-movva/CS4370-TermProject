package uga.menik.csx370.models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Represents an offer outcome for an application.
 */
public class Offer {
    private final int offerId;
    private final int applicationId;
    private final String compensation;
    private final Date startDate;
    private final Date decisionDeadline;
    private final String status;
    private final String notes;
    private final Timestamp createdAt;

    public Offer(int offerId, int applicationId, String compensation, Date startDate,
                 Date decisionDeadline, String status, String notes, Timestamp createdAt) {
        this.offerId = offerId;
        this.applicationId = applicationId;
        this.compensation = compensation;
        this.startDate = startDate;
        this.decisionDeadline = decisionDeadline;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getOfferId() {
        return offerId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getCompensation() {
        return compensation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDecisionDeadline() {
        return decisionDeadline;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
