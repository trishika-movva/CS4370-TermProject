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

import uga.menik.csx370.models.Company;

/**
 * Provides company summaries and counts.
 */
@Service
public class CompanyService {

    private final DataSource dataSource;

    @Autowired
    public CompanyService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Company> listCompaniesWithCounts(int userId) throws SQLException {
        final String sql = """
                SELECT c.company_id,
                       c.name,
                       c.industry,
                       c.hq_location,
                       c.website,
                       COUNT(a.application_id) AS application_count
                  FROM company c
                  LEFT JOIN job_posting jp ON c.company_id = jp.company_id
                  LEFT JOIN application a ON jp.job_id = a.job_id AND a.user_id = ?
                 GROUP BY c.company_id, c.name, c.industry, c.hq_location, c.website
                 ORDER BY application_count DESC, c.name ASC
                """;
        List<Company> companies = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    companies.add(new Company(
                            rs.getInt("company_id"),
                            rs.getString("name"),
                            rs.getString("industry"),
                            rs.getString("hq_location"),
                            rs.getString("website"),
                            rs.getInt("application_count")));
                }
            }
        }
        return companies;
    }
}
