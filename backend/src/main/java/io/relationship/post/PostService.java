package io.relationship.post;
import io.relationship.common.exception.*;
import io.relationship.friend.FriendGroupMappingRepository;
import io.relationship.friend.FriendRepository;
import io.relationship.group.*;
import io.relationship.post.PostEntity.VisibilityType;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final PostVisibilityGroupRepository pvgRepo;
    private final GroupRepository groupRepo;
    private final UserService userService;
    private final FriendRepository friendRepo;
    private final FriendGroupMappingRepository mappingRepo;

    @Transactional
    public PostEntity createPost(Long userId, PostDto.CreatePostRequest req) {
        UserEntity author = userService.getById(userId);
        if (req.getVisibilityType()==VisibilityType.SELECTED_GROUPS && (req.getVisibleToGroupIds()==null || req.getVisibleToGroupIds().isEmpty()))
            throw new IllegalArgumentException("SELECTED_GROUPS visibility requires at least one group");
        PostEntity post = postRepo.save(PostEntity.builder().author(author).caption(req.getCaption()).mediaUrl(req.getMediaUrl()).visibilityType(req.getVisibilityType()).build());
        if (req.getVisibilityType()==VisibilityType.SELECTED_GROUPS) saveVisibilityGroups(post,userId,req.getVisibleToGroupIds());
        return post;
    }
    private void saveVisibilityGroups(PostEntity post, Long ownerId, List<Long> groupIds) {
        for (Long gid : groupIds) {
            UserGroupEntity g = groupRepo.findById(gid).orElseThrow(()->new ResourceNotFoundException("Group",gid));
            if (!g.getOwner().getId().equals(ownerId)) throw new ForbiddenException("Group "+gid+" does not belong to you");
            pvgRepo.save(PostVisibilityGroupEntity.builder().post(post).group(g).build());
        }
    }
    @Transactional(readOnly=true)
    public PostEntity getPostById(Long postId, Long viewerId) {
        PostEntity post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post",postId));
        if (!canViewPost(post,viewerId)) throw new ForbiddenException("You do not have permission to view this post");
        return post;
    }
    public boolean canViewPost(PostEntity post, Long viewerId) {
        Long authorId = post.getAuthor().getId();
        if (authorId.equals(viewerId)) return true;
        return switch (post.getVisibilityType()) {
            case PUBLIC -> true;
            case ALL_CONNECTIONS -> friendRepo.areFriends(authorId,viewerId);
            case SELECTED_GROUPS -> {
                List<Long> postGroupIds = pvgRepo.findAllByPostId(post.getId()).stream().map(p->p.getGroup().getId()).toList();
                if (postGroupIds.isEmpty()) yield false;
                List<Long> viewerGroups = mappingRepo.findAllByOwnerIdAndFriendId(authorId,viewerId).stream().map(m->m.getGroup().getId()).toList();
                yield postGroupIds.stream().anyMatch(viewerGroups::contains);
            }
            case PRIVATE -> false;
        };
    }
    @Transactional(readOnly=true) public List<PostEntity> getMyPosts(Long userId) { return postRepo.findAllByAuthorIdOrderByCreatedAtDesc(userId, org.springframework.data.domain.PageRequest.of(0,1000)).getContent(); }
    @Transactional
    public void deletePost(Long postId, Long userId) {
        PostEntity post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post",postId));
        if (!post.getAuthor().getId().equals(userId)) throw new ForbiddenException("You can only delete your own posts");
        pvgRepo.deleteAllByPostId(postId); postRepo.delete(post);
    }
    public PostDto.PostResponse toResponse(PostEntity post) {
        List<Long> groupIds = pvgRepo.findAllByPostId(post.getId()).stream().map(p->p.getGroup().getId()).toList();
        return PostDto.PostResponse.builder().id(post.getId()).authorId(post.getAuthor().getId()).authorUsername(post.getAuthor().getUsername())
            .caption(post.getCaption()).mediaUrl(post.getMediaUrl()).visibilityType(post.getVisibilityType())
            .visibleToGroupIds(groupIds).createdAt(post.getCreatedAt()).build();
    }
}