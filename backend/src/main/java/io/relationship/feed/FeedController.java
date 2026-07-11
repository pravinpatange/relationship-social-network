package io.relationship.feed;
import io.relationship.common.response.ApiResponse;
import io.relationship.common.response.PagedResponse;
import io.relationship.post.PostDto;
import io.relationship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/feed") @RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService; private final UserService userService;
    @GetMapping public ResponseEntity<ApiResponse<PagedResponse<PostDto.PostResponse>>> feed(
            @AuthenticationPrincipal UserDetails ud,
            @RequestParam(required=false) Long groupId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size) {
        Long uid = userService.getByEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(ApiResponse.success(feedService.getFeed(uid,groupId,page,size)));
    }
}