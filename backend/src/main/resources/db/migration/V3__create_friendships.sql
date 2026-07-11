CREATE TABLE friendships (
    id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_friend_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_friend_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uk_friendship ON friendships(requester_id, receiver_id);
