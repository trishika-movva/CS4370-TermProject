package uga.menik.csx370.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.csx370.models.ApplicationView;
import uga.menik.csx370.models.InterviewRound;
import uga.menik.csx370.models.Offer;

/**
 * Handles CRUD operations for applications using raw JDBC.
 */
@Service
public class ApplicationService {

    private final DataSource dataSource;

    @Autowired
    public ApplicationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ApplicationView> getApplicationsForUser(int userId) throws SQLException {
        final String sql = """
                SELECT a.application_id,
                       c.name AS company_name,
                       jp.title,
                       jp.location,
                       jp.employment_type,
                       jp.job_level,
                       a.status,
                       a.applied_date,
                       jp.season,
                       a.source,
                       a.resume_version,
                       a.notes,
                       a.last_updated
                  FROM application a
                  JOIN job_posting jp ON a.job_id = jp.job_id
                  JOIN company c ON jp.company_id = c.company_id
                 WHERE a.user_id = ?
                 ORDER BY a.last_updated DESC
                """;

        List<ApplicationView> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapApplicationView(rs));
                }
            }
        }
        return results;
    }

    public List<ApplicationView> getRecentApplications(int userId, int limit) throws SQLException {
        final String sql = """
                SELECT a.application_id,
                       c.name AS company_name,
                       jp.title,
                       jp.location,
                       jp.employment_type,
                       jp.job_level,
                       a.status,
                       a.applied_date,
                       jp.season,
                       a.source,
                       a.resume_version,
                       a.notes,
                       a.last_updated
                  FROM application a
                  JOIN job_posting jp ON a.job_id = jp.job_id
                  JOIN company c ON jp.company_id = c.company_id
                 WHERE a.user_id = ?
                 ORDER BY a.last_updated DESC
                 LIMIT ?
                """;

        List<ApplicationView> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapApplicationView(rs));
                }
            }
        }
        return results;
    }

    public int countApplicationsForUser(int userId) throws SQLException {
        final String sql = "SELECT COUNT(*) FROM application WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countOpenApplicationsForUser(int userId) throws SQLException {
        final String sql = """
                SELECT COUNT(*) FROM application
                 WHERE user_id = ? AND (status IS NULL OR status NOT IN ('Rejected'))
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void createApplication(int userId,
                                  String companyName,
                                  String industry,
                                  String hqLocation,
                                  String website,
                                  String title,
                                  String location,
                                  String employmentType,
                                  String jobLevel,
                                  String season,
                                  Date appliedDate,
                                  String status,
                                  String source,
                                  String resumeVersion,
                                  String notes) throws SQLException {
        int companyId = ensureCompany(companyName, industry, hqLocation, website);
        int jobId = createJobPosting(companyId, title, location, employmentType, jobLevel, season);

        final String sql = """
                INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, jobId);
            stmt.setDate(3, appliedDate != null ? appliedDate : Date.valueOf(LocalDate.now()));
            stmt.setString(4, status);
            stmt.setString(5, source);
            stmt.setString(6, resumeVersion);
            stmt.setString(7, notes);
            stmt.executeUpdate();
        }
    }

    public void updateApplicationStatus(int applicationId, int userId, String status, String notes) throws SQLException {
        final String sql = """
                UPDATE application
                   SET status = ?, notes = ?, last_updated = CURRENT_TIMESTAMP
                 WHERE application_id = ? AND user_id = ?
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, notes);
            stmt.setInt(3, applicationId);
            stmt.setInt(4, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteApplication(int applicationId, int userId) throws SQLException {
        final String sql = "DELETE FROM application WHERE application_id = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public ApplicationView getApplicationDetail(int applicationId, int userId) throws SQLException {
        final String sql = """
                SELECT a.application_id,
                       c.name AS company_name,
                       jp.title,
                       jp.location,
                       jp.employment_type,
                       jp.job_level,
                       a.status,
                       a.applied_date,
                       jp.season,
                       a.source,
                       a.resume_version,
                       a.notes,
                       a.last_updated
                  FROM application a
                  JOIN job_posting jp ON a.job_id = jp.job_id
                  JOIN company c ON jp.company_id = c.company_id
                 WHERE a.application_id = ? AND a.user_id = ?
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapApplicationView(rs);
                }
            }
        }
        return null;
    }

    public List<InterviewRound> getInterviewRounds(int applicationId, int userId) throws SQLException {
        final String sql = """
                SELECT ir.interview_id,
                       ir.application_id,
                       ir.round_type,
                       ir.scheduled_date,
                       ir.status,
                       ir.feedback,
                       ir.created_at
                  FROM interview_round ir
                  JOIN application a ON ir.application_id = a.application_id
                 WHERE ir.application_id = ? AND a.user_id = ?
                 ORDER BY ir.created_at DESC
                """;
        List<InterviewRound> rounds = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rounds.add(new InterviewRound(
                            rs.getInt("interview_id"),
                            rs.getInt("application_id"),
                            rs.getString("round_type"),
                            rs.getDate("scheduled_date"),
                            rs.getString("status"),
                            rs.getString("feedback"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return rounds;
    }

    public void addInterviewRound(int applicationId, int userId, String roundType, Date scheduledDate,
                                  String status, String feedback) throws SQLException {
        if (!userOwnsApplication(applicationId, userId)) {
            throw new SQLException("Unauthorized application access");
        }
        final String sql = """
                INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setString(2, roundType);
            stmt.setDate(3, scheduledDate);
            stmt.setString(4, status);
            stmt.setString(5, feedback);
            stmt.executeUpdate();
        }
    }

    public List<Offer> getOffers(int applicationId, int userId) throws SQLException {
        final String sql = """
                SELECT o.offer_id,
                       o.application_id,
                       o.compensation,
                       o.start_date,
                       o.decision_deadline,
                       o.status,
                       o.notes,
                       o.created_at
                  FROM offer o
                  JOIN application a ON o.application_id = a.application_id
                 WHERE o.application_id = ? AND a.user_id = ?
                 ORDER BY o.created_at DESC
                """;
        List<Offer> offers = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    offers.add(new Offer(
                            rs.getInt("offer_id"),
                            rs.getInt("application_id"),
                            rs.getString("compensation"),
                            rs.getDate("start_date"),
                            rs.getDate("decision_deadline"),
                            rs.getString("status"),
                            rs.getString("notes"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return offers;
    }

    public void addOffer(int applicationId, int userId, String compensation, Date startDate,
                         Date decisionDeadline, String status, String notes) throws SQLException {
        if (!userOwnsApplication(applicationId, userId)) {
            throw new SQLException("Unauthorized application access");
        }
        final String sql = """
                INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setString(2, compensation);
            stmt.setDate(3, startDate);
            stmt.setDate(4, decisionDeadline);
            stmt.setString(5, status);
            stmt.setString(6, notes);
            stmt.executeUpdate();
        }
    }

    private int ensureCompany(String name, String industry, String hqLocation, String website) throws SQLException {
        final String select = "SELECT company_id FROM company WHERE name = ? LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("company_id");
                }
            }
        }

        final String insert = "INSERT INTO company (name, industry, hq_location, website) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, industry);
            stmt.setString(3, hqLocation);
            stmt.setString(4, website);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create or find company");
    }

    private int createJobPosting(int companyId, String title, String location, String employmentType,
                                 String jobLevel, String season) throws SQLException {
        final String insert = """
                INSERT INTO job_posting (company_id, title, location, employment_type, job_level, posting_date, season, is_open)
                VALUES (?, ?, ?, ?, ?, ?, ?, TRUE)
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, companyId);
            stmt.setString(2, title);
            stmt.setString(3, location);
            stmt.setString(4, employmentType);
            stmt.setString(5, jobLevel);
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setString(7, season);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create job posting");
    }

    private ApplicationView mapApplicationView(ResultSet rs) throws SQLException {
        int applicationId = rs.getInt("application_id");
        String companyName = rs.getString("company_name");
        String jobTitle = rs.getString("title");
        String location = rs.getString("location");
        String employmentType = rs.getString("employment_type");
        String jobLevel = rs.getString("job_level");
        String status = rs.getString("status");
        Date appliedDate = rs.getDate("applied_date");
        String season = rs.getString("season");
        String source = rs.getString("source");
        String resumeVersion = rs.getString("resume_version");
        String notes = rs.getString("notes");
        Timestamp lastUpdated = rs.getTimestamp("last_updated");

        return new ApplicationView(applicationId, companyName, jobTitle, location, employmentType, jobLevel,
                status, appliedDate, season, source, resumeVersion, notes, lastUpdated);
    }

    private boolean userOwnsApplication(int applicationId, int userId) throws SQLException {
        final String sql = "SELECT 1 FROM application WHERE application_id = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
