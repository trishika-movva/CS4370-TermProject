-- ==============================================
-- ApplicationTracker Queries Reference
-- ==============================================
USE application_tracker;

-- Auth (LoginController/UserService)
-- /login (POST)
SELECT user_id, username, email, password_hash, created_at FROM user WHERE username = ?;

-- Registration (RegistrationController/UserService)
-- /signup (POST)
INSERT INTO user (username, email, password_hash) VALUES (?, ?, ?);

-- Dashboard (DashboardController)
-- /dashboard (GET) counts + recent
SELECT COUNT(*) FROM application WHERE user_id = ?;
SELECT COUNT(*) FROM application WHERE user_id = ? AND (status IS NULL OR status NOT IN ('Rejected'));
SELECT c.company_id, c.name, c.industry, c.hq_location, c.website, COUNT(a.application_id) AS application_count
  FROM company c
  LEFT JOIN job_posting jp ON c.company_id = jp.company_id
  LEFT JOIN application a ON jp.job_id = a.job_id AND a.user_id = ?
 GROUP BY c.company_id, c.name, c.industry, c.hq_location, c.website
 ORDER BY application_count DESC, c.name ASC;
SELECT a.application_id, c.name, jp.title, jp.location, jp.employment_type, jp.job_level,
       a.status, a.applied_date, jp.season, a.source, a.resume_version, a.notes, a.last_updated
  FROM application a
  JOIN job_posting jp ON a.job_id = jp.job_id
  JOIN company c ON jp.company_id = c.company_id
 WHERE a.user_id = ?
 ORDER BY a.last_updated DESC
 LIMIT ?;

-- Applications list/create/update/delete (ApplicationController/ApplicationService)
-- /applications (GET)
SELECT ... FROM application a JOIN job_posting jp ... WHERE a.user_id = ? ORDER BY a.last_updated DESC;
-- /applications (POST)
INSERT INTO company (name, industry, hq_location, website) VALUES (?, ?, ?, ?);
INSERT INTO job_posting (company_id, title, location, employment_type, job_level, posting_date, season, is_open)
VALUES (?, ?, ?, ?, ?, ?, ?, TRUE);
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated)
VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
-- /applications/{id}/status (POST)
UPDATE application SET status = ?, notes = ?, last_updated = CURRENT_TIMESTAMP WHERE application_id = ? AND user_id = ?;
-- /applications/{id}/delete (POST)
DELETE FROM application WHERE application_id = ? AND user_id = ?;

-- Application detail + interview rounds/offers (ApplicationController/ApplicationService)
-- /applications/{id} (GET)
SELECT ... FROM application a JOIN job_posting jp ON ... JOIN company c ON ... WHERE a.application_id = ? AND a.user_id = ?;
SELECT ir.* FROM interview_round ir JOIN application a ON ir.application_id = a.application_id WHERE ir.application_id = ? AND a.user_id = ? ORDER BY ir.created_at DESC;
SELECT o.* FROM offer o JOIN application a ON o.application_id = a.application_id WHERE o.application_id = ? AND a.user_id = ? ORDER BY o.created_at DESC;
-- /applications/{id}/rounds (POST)
INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback) VALUES (?, ?, ?, ?, ?);
-- /applications/{id}/offers (POST)
INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes) VALUES (?, ?, ?, ?, ?, ?);

-- Companies summary (CompanyController/CompanyService)
-- /companies (GET)
SELECT c.company_id, c.name, c.industry, c.hq_location, c.website, COUNT(a.application_id) AS application_count
  FROM company c
  LEFT JOIN job_posting jp ON c.company_id = jp.company_id
  LEFT JOIN application a ON jp.job_id = a.job_id AND a.user_id = ?
 GROUP BY c.company_id, c.name, c.industry, c.hq_location, c.website
 ORDER BY application_count DESC, c.name ASC;
