package uga.menik.csx370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.csx370.models.JobPosting;

/**
 * Provides read helpers for job postings.
 */
@Service
public class JobPostingService {

    private final DataSource dataSource;

    @Autowired
    public JobPostingService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<JobPosting> listRecentJobPostings(int limit) throws SQLException {
        final String sql = """
                SELECT jp.job_id,
                       jp.company_id,
                       c.name AS company_name,
                       jp.title,
                       jp.location,
                       jp.employment_type,
                       jp.job_level,
                       jp.posting_date,
                       jp.application_deadline,
                       jp.season,
                       jp.is_open
                  FROM job_posting jp
                  JOIN company c ON jp.company_id = c.company_id
                 ORDER BY jp.posting_date DESC
                 LIMIT ?
                """;
        List<JobPosting> postings = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    postings.add(new JobPosting(
                            rs.getInt("job_id"),
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getString("title"),
                            rs.getString("location"),
                            rs.getString("employment_type"),
                            rs.getString("job_level"),
                            rs.getDate("posting_date"),
                            rs.getDate("application_deadline"),
                            rs.getString("season"),
                            rs.getBoolean("is_open")));
                }
            }
        }
        return postings;
    }
}
