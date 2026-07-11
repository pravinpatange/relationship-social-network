CREATE TABLE user_groups (
    id BIGSERIAL PRIMARY KEY,
    owner_user_id BIGINT NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_group_owner FOREIGN KEY (owner_user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_group_name UNIQUE (owner_user_id, group_name)
);
CREATE INDEX idx_group_owner ON user_groups(owner_user_id);
