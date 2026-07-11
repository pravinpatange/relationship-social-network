package io.relationship.comment;
import io.relationship.common.exception.*;
import io.relationship.notification.*;
import io.relationship.notification.NotificationEntity.NotificationType;
import io.relationship.post.*;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserService userService;
    private final NotificationService notifService;

    @Transactional
    public CommentEntity addComment(Long userId, Long postId, String text) {
        PostEntity post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post",postId));
        UserEntity author = userService.getById(userId);
        CommentEntity saved = commentRepo.save(CommentEntity.builder().post(post).author(author).commentText(text).build());
        notifService.createNotification(post.getAuthor().getId(), userId, NotificationType.POST_COMMENT, postId);
        return saved;
    }
    @Transactional(readOnly=true)
    public List<CommentEntity> getCommentsForPost(Long postId) {
        if (!postRepo.existsById(postId)) throw new ResourceNotFoundException("Post",postId);
        return commentRepo.findAllByPostIdOrderByCreatedAtAsc(postId);
    }
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        CommentEntity c = commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment",commentId));
        if (!c.getAuthor().getId().equals(userId)) throw new ForbiddenException("You can only delete your own comments");
        commentRepo.delete(c);
    }
    public CommentDto.CommentResponse toResponse(CommentEntity c) {
        return CommentDto.CommentResponse.builder().id(c.getId()).postId(c.getPost().getId())
            .authorId(c.getAuthor().getId()).authorUsername(c.getAuthor().getUsername())
            .commentText(c.getCommentText()).createdAt(c.getCreatedAt()).build();
    }
}