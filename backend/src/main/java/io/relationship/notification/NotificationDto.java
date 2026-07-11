package io.relationship.notification;
import lombok.*;
import java.time.LocalDateTime;
public class NotificationDto {
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NotificationResponse {
        private Long id,recipientId,actorId,referenceId;
        private String actorUsername;
        private NotificationEntity.NotificationType type;
        private boolean read;
        private LocalDateTime createdAt;
    }
}