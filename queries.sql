-- ApplicationTracker Queries
USE application_tracker;

-- Auth login (fetching user for authentication)
-- URL: /login (POST)
SELECT user_id, username, email, password_hash, created_at FROM user WHERE username = ?;
-- Auth signup (creating new user)
-- URL: /signup (POST)
INSERT INTO user (username, email, password_hash) VALUES (?, ?, ?);

-- Dashboard total applications for user
-- URL: /dashboard (GET)
SELECT COUNT(*) FROM application WHERE user_id = ?;

-- Dashboard open applications count excluding rejections
-- URL: /dashboard (GET)
SELECT COUNT(*) FROM application WHERE user_id = ? AND (status IS NULL OR status NOT IN ('Rejected'));

-- Dashboard company summary with application counts (join and aggregation)
-- URL: /dashboard (GET)
SELECT c.company_id, c.name, c.industry, c.hq_location, c.website, COUNT(a.application_id) AS application_count
  FROM company c
  LEFT JOIN job_posting jp ON c.company_id = jp.company_id
  LEFT JOIN application a ON jp.job_id = a.job_id AND a.user_id = ?
 GROUP BY c.company_id, c.name, c.industry, c.hq_location, c.website
 ORDER BY application_count DESC, c.name ASC;

-- Dashboard recent applications with joins
-- URL: /dashboard (GET)
SELECT a.application_id, c.name, jp.title, jp.location, jp.employment_type, jp.job_level,
       a.status, a.applied_date, jp.season, a.source, a.resume_version, a.notes, a.last_updated
  FROM application a
  JOIN job_posting jp ON a.job_id = jp.job_id
  JOIN company c ON jp.company_id = c.company_id
 WHERE a.user_id = ?
 ORDER BY a.last_updated DESC
 LIMIT ?;

-- Applications list for user (join for company or job fields)
-- URL: /applications (GET)
SELECT a.application_id, c.name AS company_name, jp.title, jp.location, jp.employment_type, jp.job_level,
       a.status, a.applied_date, jp.season, a.source, a.resume_version, a.notes, a.last_updated
  FROM application a
  JOIN job_posting jp ON a.job_id = jp.job_id
  JOIN company c ON jp.company_id = c.company_id
 WHERE a.user_id = ?
 ORDER BY a.last_updated DESC;

-- Create company record if needed
-- URL: /applications (POST)
INSERT INTO company (name, industry, hq_location, website) VALUES (?, ?, ?, ?);

-- Create job posting when inserting application
-- URL: /applications (POST)
INSERT INTO job_posting (company_id, title, location, employment_type, job_level, posting_date, season, is_open)
VALUES (?, ?, ?, ?, ?, ?, ?, TRUE);

-- Insert application for user
-- URL: /applications (POST)
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated)
VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);

-- Update application status or notes
-- URL: /applications/{id}/status (POST)
UPDATE application SET status = ?, notes = ?, last_updated = CURRENT_TIMESTAMP WHERE application_id = ? AND user_id = ?;

-- Delete application owned by user
-- URL: /applications/{id}/delete (POST)
DELETE FROM application WHERE application_id = ? AND user_id = ?;

-- Application detail query with joins
-- URL: /applications/{id} (GET)
SELECT a.application_id, c.name AS company_name, jp.title, jp.location, jp.employment_type, jp.job_level,
       a.status, a.applied_date, jp.season, a.source, a.resume_version, a.notes, a.last_updated
  FROM application a
  JOIN job_posting jp ON a.job_id = jp.job_id
  JOIN company c ON jp.company_id = c.company_id
 WHERE a.application_id = ? AND a.user_id = ?;

-- Interview rounds for an application (ordered from newest to oldest)
-- URL: /applications/{id} (GET)
SELECT ir.interview_id, ir.application_id, ir.round_type, ir.scheduled_date, ir.status, ir.feedback, ir.created_at
  FROM interview_round ir
  JOIN application a ON ir.application_id = a.application_id
 WHERE ir.application_id = ? AND a.user_id = ?
 ORDER BY ir.created_at DESC;

-- Offers for an application (ordered from newest to oldest)
-- URL: /applications/{id} (GET)
SELECT o.offer_id, o.application_id, o.compensation, o.start_date, o.decision_deadline, o.status, o.notes, o.created_at
  FROM offer o
  JOIN application a ON o.application_id = a.application_id
 WHERE o.application_id = ? AND a.user_id = ?
 ORDER BY o.created_at DESC;

-- Add interview round
-- URL: /applications/{id}/rounds (POST)
INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback) VALUES (?, ?, ?, ?, ?);

-- Add offer
-- URL: /applications/{id}/offers (POST)
INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes) VALUES (?, ?, ?, ?, ?, ?);

-- Companies summary with user’s application counts (join and aggregation)
-- URL: /companies (GET)
SELECT c.company_id, c.name, c.industry, c.hq_location, c.website, COUNT(a.application_id) AS application_count
  FROM company c
  LEFT JOIN job_posting jp ON c.company_id = jp.company_id
  LEFT JOIN application a ON jp.job_id = a.job_id AND a.user_id = ?
 GROUP BY c.company_id, c.name, c.industry, c.hq_location, c.website
 ORDER BY application_count DESC, c.name ASC;

-- Status summary counts
-- URL: /reports (GET)
SELECT COALESCE(status, 'Unknown') AS status, COUNT(*) AS total FROM application WHERE user_id = ? GROUP BY COALESCE(status, 'Unknown');

-- Upcoming interviews with job/company join
-- URL: /reports (GET)
SELECT ir.interview_id, ir.application_id, ir.round_type, ir.status, ir.scheduled_date, c.name, jp.title
  FROM interview_round ir
  JOIN application a ON ir.application_id = a.application_id
  JOIN job_posting jp ON a.job_id = jp.job_id
  JOIN company c ON jp.company_id = c.company_id
 WHERE a.user_id = ? AND ir.scheduled_date IS NOT NULL
 ORDER BY ir.scheduled_date ASC
 LIMIT ?;

-- Recent offers with job/company join
-- URL: /reports (GET)
SELECT o.offer_id, o.application_id, o.compensation, o.decision_deadline, o.status, c.name, jp.title
  FROM offer o
  JOIN application a ON o.application_id = a.application_id
  JOIN job_posting jp ON a.job_id = jp.job_id
  JOIN company c ON jp.company_id = c.company_id
 WHERE a.user_id = ?
 ORDER BY o.created_at DESC
 LIMIT ?;
