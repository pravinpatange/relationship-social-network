package io.relationship.like;
import lombok.*;
public class LikeDto {
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class LikeResponse { private Long postId; private int likeCount; private boolean likedByMe; }
}