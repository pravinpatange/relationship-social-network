package io.relationship.follower;
import io.relationship.common.exception.*;
import io.relationship.notification.*;
import io.relationship.notification.NotificationEntity.NotificationType;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepository repo; private final UserService userService; private final NotificationService notifService;
    @Transactional
    public FollowerEntity follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) throw new IllegalArgumentException("Cannot follow yourself");
        if (repo.existsByFollowerIdAndFollowingId(followerId,followingId)) throw new DuplicateResourceException("Already following");
        FollowerEntity saved = repo.save(FollowerEntity.builder().follower(userService.getById(followerId)).following(userService.getById(followingId)).build());
        notifService.createNotification(followingId,followerId,NotificationType.FOLLOW,null);
        return saved;
    }
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        FollowerEntity r = repo.findByFollowerIdAndFollowingId(followerId,followingId).orElseThrow(()->new ResourceNotFoundException("Not following this user"));
        repo.delete(r);
    }
    @Transactional(readOnly=true) public List<FollowerEntity> getFollowers(Long uid) { return repo.findAllByFollowingId(uid); }
    @Transactional(readOnly=true) public List<FollowerEntity> getFollowing(Long uid) { return repo.findAllByFollowerId(uid); }
    public FollowerDto.FollowerResponse toResponse(FollowerEntity e) {
        return FollowerDto.FollowerResponse.builder().id(e.getId()).followerUserId(e.getFollower().getId()).followerUsername(e.getFollower().getUsername())
            .followingUserId(e.getFollowing().getId()).followingUsername(e.getFollowing().getUsername()).createdAt(e.getCreatedAt()).build();
    }
}