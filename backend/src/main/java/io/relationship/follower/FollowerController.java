package io.relationship.follower;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/follow") @RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService; private final UserService userService;
    @PostMapping("/{uid}") public ResponseEntity<ApiResponse<FollowerDto.FollowerResponse>> follow(@AuthenticationPrincipal UserDetails ud, @PathVariable Long uid) {
        return ResponseEntity.ok(ApiResponse.success("Following", followerService.toResponse(followerService.follow(id(ud),uid))));
    }
    @DeleteMapping("/{uid}") public ResponseEntity<ApiResponse<Void>> unfollow(@AuthenticationPrincipal UserDetails ud, @PathVariable Long uid) {
        followerService.unfollow(id(ud),uid); return ResponseEntity.ok(ApiResponse.success("Unfollowed",null));
    }
    @GetMapping("/followers") public ResponseEntity<ApiResponse<List<FollowerDto.FollowerResponse>>> followers(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(followerService.getFollowers(id(ud)).stream().map(followerService::toResponse).toList()));
    }
    @GetMapping("/following") public ResponseEntity<ApiResponse<List<FollowerDto.FollowerResponse>>> following(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(followerService.getFollowing(id(ud)).stream().map(followerService::toResponse).toList()));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}