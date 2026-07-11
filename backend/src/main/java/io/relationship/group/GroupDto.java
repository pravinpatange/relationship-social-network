package io.relationship.group;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
public class GroupDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CreateGroupRequest {
        @NotBlank @Size(min=1,max=100) private String groupName;
        @Size(max=500) private String description;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UpdateGroupRequest {
        @Size(min=1,max=100) private String groupName;
        @Size(max=500) private String description;
    }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class GroupResponse {
        private Long id, ownerUserId;
        private String groupName, description;
        private LocalDateTime createdAt;
    }
}