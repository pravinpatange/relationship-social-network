CREATE TABLE friend_group_mapping (
    id BIGSERIAL PRIMARY KEY,
    owner_user_id BIGINT NOT NULL,
    friend_user_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mapping_owner FOREIGN KEY (owner_user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_mapping_friend FOREIGN KEY (friend_user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_mapping_group FOREIGN KEY (group_id) REFERENCES user_groups(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uk_friend_group ON friend_group_mapping(owner_user_id, friend_user_id, group_id);
CREATE INDEX idx_mapping_owner ON friend_group_mapping(owner_user_id);
CREATE INDEX idx_mapping_friend ON friend_group_mapping(friend_user_id);
CREATE INDEX idx_mapping_group ON friend_group_mapping(group_id);
