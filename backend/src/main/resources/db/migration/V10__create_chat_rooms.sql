CREATE TABLE chat_rooms (
    id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    context_group_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_room_user1 FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_room_user2 FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_room_group FOREIGN KEY (context_group_id) REFERENCES user_groups(id) ON DELETE SET NULL
);
CREATE UNIQUE INDEX uk_chat_room ON chat_rooms(LEAST(user1_id,user2_id), GREATEST(user1_id,user2_id), COALESCE(context_group_id,0));
