package io.relationship.chat;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
public class ChatDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class StartChatRequest {
        @NotNull(message="Other user ID is required") private Long otherUserId;
        private Long contextGroupId;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class SendMessageRequest { @NotBlank(message="Message cannot be blank") private String message; }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RoomResponse {
        private Long id,user1Id,user2Id,contextGroupId;
        private String user1Username,user2Username,contextGroupName;
        private LocalDateTime createdAt;
    }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MessageResponse {
        private Long id,roomId,senderId;
        private String senderUsername,message;
        private LocalDateTime createdAt;
    }
}