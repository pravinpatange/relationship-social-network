CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    caption TEXT,
    media_url TEXT,
    visibility_type VARCHAR(30) NOT NULL DEFAULT 'ALL_CONNECTIONS',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_post_user ON posts(user_id);
CREATE INDEX idx_post_created ON posts(created_at DESC);
CREATE INDEX idx_post_visibility ON posts(visibility_type);
