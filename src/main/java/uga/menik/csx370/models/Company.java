package uga.menik.csx370.models;

/**
 * Represents a company that posts jobs.
 */
public class Company {
    private final int companyId;
    private final String name;
    private final String industry;
    private final String hqLocation;
    private final String website;
    private final int applicationCount;

    public Company(int companyId, String name, String industry, String hqLocation, String website, int applicationCount) {
        this.companyId = companyId;
        this.name = name;
        this.industry = industry;
        this.hqLocation = hqLocation;
        this.website = website;
        this.applicationCount = applicationCount;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public String getHqLocation() {
        return hqLocation;
    }

    public String getWebsite() {
        return website;
    }

    public int getApplicationCount() {
        return applicationCount;
    }
}
