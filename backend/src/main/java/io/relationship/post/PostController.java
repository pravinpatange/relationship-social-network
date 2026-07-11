package io.relationship.post;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/posts") @RequiredArgsConstructor
public class PostController {
    private final PostService postService; private final UserService userService;
    @PostMapping public ResponseEntity<ApiResponse<PostDto.PostResponse>> create(@AuthenticationPrincipal UserDetails ud, @Valid @RequestBody PostDto.CreatePostRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Post created", postService.toResponse(postService.createPost(id(ud),req))));
    }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<PostDto.PostResponse>> get(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(postService.toResponse(postService.getPostById(id,id(ud)))));
    }
    @GetMapping("/me") public ResponseEntity<ApiResponse<List<PostDto.PostResponse>>> mine(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(postService.getMyPosts(id(ud)).stream().map(postService::toResponse).toList()));
    }
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        postService.deletePost(id,id(ud)); return ResponseEntity.ok(ApiResponse.success("Post deleted",null));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}