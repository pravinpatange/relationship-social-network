-- V1__create_users.sql
-- Creates the users table for the Relationship-Aware Social Network

CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50)     NOT NULL UNIQUE,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    display_name    VARCHAR(100),
    profile_picture_url TEXT,
    bio             TEXT,
    location        VARCHAR(100),
    website         VARCHAR(255),
    account_type    VARCHAR(20)     NOT NULL DEFAULT 'PRIVATE',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email    ON users(email);

COMMENT ON TABLE  users              IS 'Platform users';
COMMENT ON COLUMN users.account_type IS 'PUBLIC or PRIVATE';
