package io.relationship.mode;
import lombok.*;
import java.time.LocalDateTime;
public class ModeDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ChangeModeRequest { private Long groupId; }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ModeResponse { private Long userId,activeGroupId; private String activeMode,activeGroupName; private LocalDateTime updatedAt; }
}