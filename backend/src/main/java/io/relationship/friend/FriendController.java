package io.relationship.friend;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/friends") @RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService; private final UserService userService;
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<FriendDto.FriendshipResponse>> send(@AuthenticationPrincipal UserDetails ud, @Valid @RequestBody FriendDto.SendRequestRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Friend request sent", friendService.toResponse(friendService.sendRequest(id(ud),req.getReceiverId()))));
    }
    @PostMapping("/{fId}/accept") public ResponseEntity<ApiResponse<FriendDto.FriendshipResponse>> accept(@AuthenticationPrincipal UserDetails ud, @PathVariable Long fId) {
        return ResponseEntity.ok(ApiResponse.success("Accepted", friendService.toResponse(friendService.acceptRequest(fId,id(ud)))));
    }
    @PostMapping("/{fId}/reject") public ResponseEntity<ApiResponse<FriendDto.FriendshipResponse>> reject(@AuthenticationPrincipal UserDetails ud, @PathVariable Long fId) {
        return ResponseEntity.ok(ApiResponse.success("Rejected", friendService.toResponse(friendService.rejectRequest(fId,id(ud)))));
    }
    @DeleteMapping("/{friendId}") public ResponseEntity<ApiResponse<Void>> unfriend(@AuthenticationPrincipal UserDetails ud, @PathVariable Long friendId) {
        friendService.unfriend(id(ud),friendId); return ResponseEntity.ok(ApiResponse.success("Unfriended",null));
    }
    @PostMapping("/{userId}/block") public ResponseEntity<ApiResponse<FriendDto.FriendshipResponse>> block(@AuthenticationPrincipal UserDetails ud, @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Blocked", friendService.toResponse(friendService.blockUser(id(ud),userId))));
    }
    @DeleteMapping("/{userId}/block") public ResponseEntity<ApiResponse<Void>> unblock(@AuthenticationPrincipal UserDetails ud, @PathVariable Long userId) {
        friendService.unblockUser(id(ud),userId); return ResponseEntity.ok(ApiResponse.success("Unblocked",null));
    }
    @GetMapping public ResponseEntity<ApiResponse<List<FriendDto.FriendshipResponse>>> friends(@AuthenticationPrincipal UserDetails ud) {
        Long id = id(ud);
        List<FriendshipEntity> accepted = friendService.getAcceptedFriends(id);
        System.out.println("DEBUG API /friends for user " + id + " returned " + accepted.size() + " rows");
        return ResponseEntity.ok(ApiResponse.success(accepted.stream().map(friendService::toResponse).toList()));
    }
    @GetMapping("/requests") public ResponseEntity<ApiResponse<List<FriendDto.FriendshipResponse>>> pending(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(friendService.getPendingRequests(id(ud)).stream().map(friendService::toResponse).toList()));
    }
    @GetMapping("/requests/sent") public ResponseEntity<ApiResponse<List<FriendDto.FriendshipResponse>>> sent(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(friendService.getSentRequests(id(ud)).stream().map(friendService::toResponse).toList()));
    }
    @PostMapping("/{fId}/groups") public ResponseEntity<ApiResponse<Void>> assignGroups(@AuthenticationPrincipal UserDetails ud, @PathVariable Long fId, @Valid @RequestBody FriendDto.AssignGroupsRequest req) {
        friendService.assignFriendToGroups(id(ud),fId,req.getGroupIds()); return ResponseEntity.ok(ApiResponse.success("Groups assigned",null));
    }
    @GetMapping("/{fId}/groups") public ResponseEntity<ApiResponse<List<FriendDto.FriendGroupResponse>>> groups(@AuthenticationPrincipal UserDetails ud, @PathVariable Long fId) {
        return ResponseEntity.ok(ApiResponse.success(friendService.getFriendGroups(id(ud),fId)));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}