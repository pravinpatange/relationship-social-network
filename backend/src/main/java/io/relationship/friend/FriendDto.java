package io.relationship.friend;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
public class FriendDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class SendRequestRequest { @NotNull private Long receiverId; }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class AssignGroupsRequest { @NotEmpty private List<Long> groupIds; }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class FriendshipResponse { private Long id,requesterId,receiverId; private String requesterUsername,receiverUsername,status; private LocalDateTime createdAt; }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class FriendGroupResponse { private Long groupId; private String groupName; }
}