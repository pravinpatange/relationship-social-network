package io.relationship.follower;
import lombok.*;
import java.time.LocalDateTime;
public class FollowerDto {
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class FollowerResponse { private Long id,followerUserId,followingUserId; private String followerUsername,followingUsername; private LocalDateTime createdAt; }
}