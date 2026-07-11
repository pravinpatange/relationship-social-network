CREATE TABLE user_modes (
    user_id BIGINT PRIMARY KEY,
    active_group_id BIGINT,
    active_mode VARCHAR(30) NOT NULL DEFAULT 'ALL',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mode_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_mode_group FOREIGN KEY (active_group_id) REFERENCES user_groups(id) ON DELETE SET NULL
);
