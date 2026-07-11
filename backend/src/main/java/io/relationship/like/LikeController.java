package io.relationship.like;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/posts/{postId}/likes") @RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService; private final UserService userService;
    @PostMapping public ResponseEntity<ApiResponse<LikeDto.LikeResponse>> like(@AuthenticationPrincipal UserDetails ud, @PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success("Post liked",likeService.likePost(id(ud),postId)));
    }
    @DeleteMapping public ResponseEntity<ApiResponse<LikeDto.LikeResponse>> unlike(@AuthenticationPrincipal UserDetails ud, @PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success("Post unliked",likeService.unlikePost(id(ud),postId)));
    }
    @GetMapping public ResponseEntity<ApiResponse<LikeDto.LikeResponse>> status(@AuthenticationPrincipal UserDetails ud, @PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(likeService.getLikeStatus(id(ud),postId)));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}