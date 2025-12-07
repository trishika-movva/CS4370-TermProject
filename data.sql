-- ApplicationTracker example DML
USE application_tracker;

-- Demo user (password: password)
INSERT INTO user (username, email, password_hash)
VALUES ('demo', 'demo@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiI84AHKp8cG3hNZEd0/ii16r1d92PO');

-- Companies
INSERT INTO company (name, industry, hq_location, website) VALUES
('Acme Robotics', 'Robotics', 'San Francisco, CA', 'https://acmerobotics.example.com'),
('ByteForge', 'Software', 'Austin, TX', 'https://byteforge.example.com'),
('GreenTech Labs', 'Clean Energy', 'Denver, CO', 'https://greentech.example.com'),
('SkyPort', 'Aviation', 'Seattle, WA', 'https://skyport.example.com'),
('Nova Health', 'Healthcare', 'Boston, MA', 'https://novahealth.example.com');

-- Job postings
INSERT INTO job_posting (company_id, title, location, employment_type, job_level, posting_date, application_deadline, season, is_open) VALUES
(1, 'Software Engineer Intern', 'San Francisco, CA', 'Internship', 'SWE Intern', '2024-05-01', '2024-06-15', 'Summer 2025', TRUE),
(1, 'Robotics QA Engineer', 'San Francisco, CA', 'Full-time', 'Entry', '2024-04-12', '2024-06-01', 'Fall 2024', TRUE),
(2, 'Backend Developer', 'Remote', 'Full-time', 'New Grad', '2024-04-05', '2024-06-20', 'Fall 2024', TRUE),
(2, 'Frontend Developer Intern', 'Austin, TX', 'Internship', 'SWE Intern', '2024-03-25', '2024-05-31', 'Summer 2025', TRUE),
(3, 'Data Analyst Intern', 'Denver, CO', 'Internship', 'Data Intern', '2024-04-18', '2024-06-10', 'Summer 2025', TRUE),
(3, 'Energy Systems Engineer', 'Denver, CO', 'Full-time', 'New Grad', '2024-04-10', '2024-06-05', 'Fall 2024', TRUE),
(4, 'Avionics Engineer', 'Seattle, WA', 'Full-time', 'New Grad', '2024-03-30', '2024-05-30', 'Fall 2024', TRUE),
(5, 'Healthcare Product Manager', 'Boston, MA', 'Full-time', 'Associate', '2024-04-08', '2024-06-25', 'Fall 2024', TRUE);

-- Applications for demo user (user_id = 1)
INSERT INTO application (user_id, job_id, applied_date, status, source, resume_version, notes) VALUES
(1, 1, '2024-05-02', 'Applied', 'LinkedIn', 'Resume v2', 'Submitted via company portal'),
(1, 2, '2024-05-05', 'Interviewing', 'Referral', 'Resume v3', 'Initial phone screen complete'),
(1, 3, '2024-05-01', 'OA', 'Company Site', 'Resume v1', 'OA received, due next week'),
(1, 4, '2024-04-28', 'Rejected', 'LinkedIn', 'Resume v1', 'Short rejection email'),
(1, 5, '2024-05-03', 'Interviewing', 'Campus', 'Resume v2', 'Waiting on next round invite'),
(1, 6, '2024-05-04', 'Applied', 'Indeed', 'Resume v2', 'Recruiter reached out on LinkedIn'),
(1, 7, '2024-04-26', 'Offer', 'Referral', 'Resume v1', 'Onsite completed'),
(1, 8, '2024-05-06', 'Applied', 'LinkedIn', 'Resume v2', 'PM role with healthcare focus');

-- Interview rounds
INSERT INTO interview_round (application_id, round_type, scheduled_date, status, feedback) VALUES
(2, 'Phone Screen', '2024-05-07', 'Completed', 'Good culture fit; awaiting decision'),
(2, 'Technical Round', '2024-05-12', 'Scheduled', NULL),
(5, 'OA', '2024-05-06', 'Completed', 'Passed coding OA'),
(5, 'Technical Interview', '2024-05-10', 'Scheduled', NULL),
(7, 'Onsite', '2024-05-02', 'Completed', 'Strong performance'),
(3, 'OA', '2024-05-08', 'Pending feedback', NULL);

-- Offers
INSERT INTO offer (application_id, compensation, start_date, decision_deadline, status, notes) VALUES
(7, '$120k base + $15k bonus', '2024-09-09', '2024-05-20', 'Pending', 'Negotiating relocation'),
(7, '$125k base + $10k bonus', '2024-09-09', '2024-05-22', 'Accepted', 'Counter offer accepted'),
(2, '$115k base + equity', '2024-09-16', '2024-05-25', 'Pending', 'Awaiting official letter');
