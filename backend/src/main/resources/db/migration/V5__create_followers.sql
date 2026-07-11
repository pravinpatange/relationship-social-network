CREATE TABLE followers (
    id BIGSERIAL PRIMARY KEY,
    follower_user_id BIGINT NOT NULL,
    following_user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_follower FOREIGN KEY (follower_user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_following FOREIGN KEY (following_user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uk_followers ON followers(follower_user_id, following_user_id);
