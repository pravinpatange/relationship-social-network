CREATE TABLE post_visibility_groups (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    CONSTRAINT fk_pvg_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_pvg_group FOREIGN KEY (group_id) REFERENCES user_groups(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uk_post_visibility ON post_visibility_groups(post_id, group_id);
