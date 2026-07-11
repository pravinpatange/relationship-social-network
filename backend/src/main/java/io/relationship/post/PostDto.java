package io.relationship.post;
import io.relationship.post.PostEntity.VisibilityType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
public class PostDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CreatePostRequest {
        private String caption, mediaUrl;
        @NotNull private VisibilityType visibilityType;
        private List<Long> visibleToGroupIds;
    }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PostResponse {
        private Long authorId,id;
        private String authorUsername,caption,mediaUrl;
        private VisibilityType visibilityType;
        private List<Long> visibleToGroupIds;
        private int likeCount,commentCount;
        private LocalDateTime createdAt;
    }
}