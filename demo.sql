USE application_tracker;

-- Ensure demo user exists (password = "password")
INSERT INTO user (username, email, password_hash)
VALUES ('trishika', 'trishika@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiI84AHKp8cG3hNZEd0/ii16r1d92PO')
ON DUPLICATE KEY UPDATE username = username;

-- Capture user_id
SET @uid := (SELECT user_id FROM user WHERE username = 'trishika');

-- Sample applications using existing job IDs from data.sql (adjust if needed)
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated) VALUES
(@uid, 10001, '2024-11-15', 'Interviewing', 'LinkedIn',    'Resume v3', 'Phone screen done', NOW()),
(@uid, 10002, '2024-11-20', 'OA',           'Company site','Resume v3', 'OA due next week', NOW()),
(@uid, 10003, '2024-11-10', 'Offer',        'Referral',    'Resume v2', 'Offer received', NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), notes = VALUES(notes);

-- Interview rounds for the first application
INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback)
SELECT a.application_id, 'Technical Round', '2024-12-05', 'Scheduled', NULL
  FROM application a WHERE a.user_id = @uid AND a.job_id = 10001
ON DUPLICATE KEY UPDATE round_type = round_type;

INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback)
SELECT a.application_id, 'Onsite', '2024-12-12', 'Scheduled', NULL
  FROM application a WHERE a.user_id = @uid AND a.job_id = 10001
ON DUPLICATE KEY UPDATE round_type = round_type;

-- Offer for the third application
INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes)
SELECT a.application_id, '$120k base + $15k bonus', '2025-02-01', '2024-12-20', 'Pending', 'Negotiating relocation'
  FROM application a WHERE a.user_id = @uid AND a.job_id = 10003
ON DUPLICATE KEY UPDATE status = status;
