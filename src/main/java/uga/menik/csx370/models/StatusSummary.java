package uga.menik.csx370.models;

/**
 * Represents a count grouped by application status.
 */
public class StatusSummary {
    private final String status;
    private final int count;

    public StatusSummary(String status, int count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }
}
