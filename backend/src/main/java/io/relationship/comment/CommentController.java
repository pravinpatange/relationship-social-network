package io.relationship.comment;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/posts/{postId}/comments") @RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService; private final UserService userService;
    @PostMapping public ResponseEntity<ApiResponse<CommentDto.CommentResponse>> add(
            @AuthenticationPrincipal UserDetails ud, @PathVariable Long postId,
            @Valid @RequestBody CommentDto.AddCommentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Comment added",
            commentService.toResponse(commentService.addComment(id(ud),postId,req.getCommentText()))));
    }
    @GetMapping public ResponseEntity<ApiResponse<List<CommentDto.CommentResponse>>> list(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getCommentsForPost(postId).stream().map(commentService::toResponse).toList()));
    }
    @DeleteMapping("/{commentId}") public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserDetails ud, @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId,id(ud));
        return ResponseEntity.ok(ApiResponse.success("Comment deleted",null));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}