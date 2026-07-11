package io.relationship.like;
import io.relationship.common.exception.*;
import io.relationship.notification.*;
import io.relationship.notification.NotificationEntity.NotificationType;
import io.relationship.post.*;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepo;
    private final PostRepository postRepo;
    private final UserService userService;
    private final NotificationService notifService;

    @Transactional
    public LikeDto.LikeResponse likePost(Long userId, Long postId) {
        PostEntity post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post",postId));
        if (likeRepo.existsByPostIdAndUserId(postId,userId)) throw new DuplicateResourceException("You already liked this post");
        likeRepo.save(LikeEntity.builder().post(post).user(userService.getById(userId)).build());
        notifService.createNotification(post.getAuthor().getId(),userId,NotificationType.POST_LIKE,postId);
        return LikeDto.LikeResponse.builder().postId(postId).likeCount(likeRepo.countByPostId(postId)).likedByMe(true).build();
    }
    @Transactional
    public LikeDto.LikeResponse unlikePost(Long userId, Long postId) {
        if (!likeRepo.existsByPostIdAndUserId(postId,userId)) throw new ResourceNotFoundException("You have not liked this post");
        likeRepo.deleteByPostIdAndUserId(postId,userId);
        return LikeDto.LikeResponse.builder().postId(postId).likeCount(likeRepo.countByPostId(postId)).likedByMe(false).build();
    }
    @Transactional(readOnly=true)
    public LikeDto.LikeResponse getLikeStatus(Long userId, Long postId) {
        if (!postRepo.existsById(postId)) throw new ResourceNotFoundException("Post",postId);
        return LikeDto.LikeResponse.builder().postId(postId).likeCount(likeRepo.countByPostId(postId)).likedByMe(likeRepo.existsByPostIdAndUserId(postId,userId)).build();
    }
}