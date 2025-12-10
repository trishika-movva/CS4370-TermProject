package uga.menik.csx370.models;

import java.sql.Date;

/**
 * Represents an offer record in a list view.
 */
public class OfferItem {
    private final int offerId;
    private final int applicationId;
    private final String companyName;
    private final String jobTitle;
    private final String status;
    private final String compensation;
    private final Date decisionDeadline;

    public OfferItem(int offerId, int applicationId, String companyName, String jobTitle,
                     String status, String compensation, Date decisionDeadline) {
        this.offerId = offerId;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.status = status;
        this.compensation = compensation;
        this.decisionDeadline = decisionDeadline;
    }

    public int getOfferId() {
        return offerId;
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

    public String getStatus() {
        return status;
    }

    public String getCompensation() {
        return compensation;
    }

    public Date getDecisionDeadline() {
        return decisionDeadline;
    }
}
