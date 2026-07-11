package io.relationship.comment;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
public class CommentDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class AddCommentRequest { @NotBlank(message="Comment text is required") private String commentText; }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CommentResponse { private Long id,postId,authorId; private String authorUsername,commentText; private LocalDateTime createdAt; }
}