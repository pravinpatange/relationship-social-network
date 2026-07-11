package io.relationship.chat;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/chats") @RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService; private final UserService userService;
    @PostMapping("/rooms") public ResponseEntity<ApiResponse<ChatDto.RoomResponse>> startRoom(
            @AuthenticationPrincipal UserDetails ud, @Valid @RequestBody ChatDto.StartChatRequest req) {
        return ResponseEntity.ok(ApiResponse.success(chatService.toRoomResponse(chatService.getOrCreateRoom(id(ud),req.getOtherUserId(),req.getContextGroupId()))));
    }
    @GetMapping("/rooms") public ResponseEntity<ApiResponse<List<ChatDto.RoomResponse>>> myRooms(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(chatService.getMyRooms(id(ud)).stream().map(chatService::toRoomResponse).toList()));
    }
    @PostMapping("/rooms/{roomId}/messages") public ResponseEntity<ApiResponse<ChatDto.MessageResponse>> send(
            @AuthenticationPrincipal UserDetails ud, @PathVariable Long roomId, @Valid @RequestBody ChatDto.SendMessageRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Message sent",chatService.toMsgResponse(chatService.sendMessage(id(ud),roomId,req.getMessage()))));
    }
    @GetMapping("/rooms/{roomId}/messages") public ResponseEntity<ApiResponse<List<ChatDto.MessageResponse>>> messages(
            @AuthenticationPrincipal UserDetails ud, @PathVariable Long roomId) {
        return ResponseEntity.ok(ApiResponse.success(chatService.getMessages(id(ud),roomId).stream().map(chatService::toMsgResponse).toList()));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}