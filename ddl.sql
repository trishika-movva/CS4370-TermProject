-- Create the database
CREATE DATABASE IF NOT EXISTS application_tracker;

-- Use the database
USE application_tracker;

-- Users table for login/signup
CREATE TABLE IF NOT EXISTS user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Company table
CREATE TABLE IF NOT EXISTS company (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    industry VARCHAR(255),
    hq_location VARCHAR(255),
    website VARCHAR(255)
);

-- Job posting table
CREATE TABLE IF NOT EXISTS job_posting (
    job_id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    employment_type VARCHAR(50),
    job_level VARCHAR(100),
    posting_date DATE,
    application_deadline DATE,
    season VARCHAR(50),
    is_open BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- Application table
CREATE TABLE IF NOT EXISTS application (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    job_id INT NOT NULL,
    applied_date DATE,
    status VARCHAR(50),
    source VARCHAR(100),
    resume_version VARCHAR(100),
    notes TEXT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (job_id) REFERENCES job_posting(job_id)
);

-- Interview rounds for each application
CREATE TABLE IF NOT EXISTS interview_round (
    interview_id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT NOT NULL,
    round_type VARCHAR(100), -- Phone Screen, Technical, Onsite
    scheduled_date DATE,
    status VARCHAR(50),      -- Scheduled, Completed, Pending feedback
    feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES application(application_id) ON DELETE CASCADE
);

-- Offers associated with an application
CREATE TABLE IF NOT EXISTS offer (
    offer_id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT NOT NULL,
    compensation VARCHAR(255),
    start_date DATE,
    decision_deadline DATE,
    status VARCHAR(50), -- Pending, Accepted, Declined
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES application(application_id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_app_user_updated ON application(user_id, last_updated);
CREATE INDEX idx_ir_app_sched ON interview_round(application_id, scheduled_date);
CREATE INDEX idx_offer_app_created ON offer(application_id, created_at);
