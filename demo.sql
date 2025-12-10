USE application_tracker;

-- Ensure users exist (password = "password")
INSERT INTO user (username, email, password_hash)
VALUES 
('trishika', 'trishika@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiI84AHKp8cG3hNZEd0/ii16r1d92PO'),
('demo', 'demo@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiI84AHKp8cG3hNZEd0/ii16r1d92PO'),
('user1', 'user1@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiI84AHKp8cG3hNZEd0/ii16r1d92PO')
ON DUPLICATE KEY UPDATE username = username;

-- Capture user_ids
SET @uid_trishika := (SELECT user_id FROM user WHERE username = 'trishika');
SET @uid_demo := (SELECT user_id FROM user WHERE username = 'demo');
SET @uid_user1 := (SELECT user_id FROM user WHERE username = 'user1');

-- Clear prior demo data for these users
DELETE FROM interview_round WHERE application_id IN (SELECT application_id FROM application WHERE user_id IN (@uid_trishika, @uid_demo, @uid_user1));
DELETE FROM offer WHERE application_id IN (SELECT application_id FROM application WHERE user_id IN (@uid_trishika, @uid_demo, @uid_user1));
DELETE FROM application WHERE user_id IN (@uid_trishika, @uid_demo, @uid_user1);

-- Applications for trishika
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated) VALUES
(@uid_trishika, 10001, '2024-11-15', 'Interviewing', 'LinkedIn',    'Resume v3', 'Phone screen done', NOW()),
(@uid_trishika, 10002, '2024-11-20', 'OA',           'Company site','Resume v3', 'OA due next week', NOW()),
(@uid_trishika, 10003, '2024-11-10', 'Offer',        'Referral',    'Resume v2', 'Offer received', NOW());

-- Applications for demo
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated) VALUES
(@uid_demo, 10004, '2024-11-05', 'Applied',      'Indeed',   'Resume v1', 'Waiting on recruiter', NOW()),
(@uid_demo, 10005, '2024-11-08', 'Interviewing', 'Referral', 'Resume v2', 'Technical round scheduled', NOW());

-- Applications for user1
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes, last_updated) VALUES
(@uid_user1, 10006, '2024-11-12', 'OA',     'Company site', 'Resume v1', 'OA in progress', NOW()),
(@uid_user1, 10007, '2024-11-18', 'Offer',  'LinkedIn',     'Resume v2', 'Offer received', NOW());

-- Interview rounds for trishika (job 10001)
SET @app_t1 := (SELECT application_id FROM application WHERE user_id=@uid_trishika AND job_id=10001 LIMIT 1);
INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback) VALUES
(@app_t1, 'Technical Round', '2024-12-05', 'Scheduled', NULL),
(@app_t1, 'Onsite',          '2024-12-12', 'Scheduled', NULL);

-- Offer for trishika (job 10003)
SET @app_t3 := (SELECT application_id FROM application WHERE user_id=@uid_trishika AND job_id=10003 LIMIT 1);
INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes) VALUES
(@app_t3, '$120k base + $15k bonus', '2025-02-01', '2024-12-20', 'Pending', 'Negotiating relocation');

-- Interview round for demo (job 10005)
SET @app_d2 := (SELECT application_id FROM application WHERE user_id=@uid_demo AND job_id=10005 LIMIT 1);
INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback) VALUES
(@app_d2, 'Technical Round', '2024-12-08', 'Scheduled', NULL);

-- Offer for user1 (job 10007)
SET @app_u2 := (SELECT application_id FROM application WHERE user_id=@uid_user1 AND job_id=10007 LIMIT 1);
INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes) VALUES
(@app_u2, '$115k base + $10k bonus', '2025-01-15', '2024-12-22', 'Pending', 'Considering options');
