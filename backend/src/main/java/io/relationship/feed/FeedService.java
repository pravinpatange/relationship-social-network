package io.relationship.feed;
import io.relationship.common.response.PagedResponse;
import io.relationship.friend.*;
import io.relationship.friend.FriendshipEntity.FriendshipStatus;
import io.relationship.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service @RequiredArgsConstructor
public class FeedService {
    private final PostRepository postRepo;
    private final FriendRepository friendRepo;
    private final FriendGroupMappingRepository mappingRepo;
    private final PostService postService;

    @Transactional(readOnly=true)
    public PagedResponse<PostDto.PostResponse> getFeed(Long userId, Long groupId, int page, int size) {
        List<Long> friendIds = friendRepo.findAllByUserIdAndStatus(userId,FriendshipStatus.ACCEPTED).stream()
            .map(f->f.getRequester().getId().equals(userId)?f.getReceiver().getId():f.getRequester().getId()).toList();

        Set<Long> seen = new LinkedHashSet<>();
        List<PostEntity> merged = new ArrayList<>();

        if (groupId==null) {
            addUnique(merged,seen,postRepo.findAllPublicPosts());
            if (!friendIds.isEmpty()) addUnique(merged,seen,postRepo.findConnectionsPosts(friendIds));
            List<Long> myGroups = mappingRepo.findGroupsWhereUserIsMember(userId);
            if (!myGroups.isEmpty()) addUnique(merged,seen,postRepo.findPostsVisibleToGroups(myGroups));
        } else {
            addUnique(merged,seen,postRepo.findAllPublicPosts());
            List<Long> friendsInGroup = mappingRepo.findAllByOwnerIdAndGroupId(userId,groupId).stream().map(m->m.getFriend().getId()).toList();
            if (!friendsInGroup.isEmpty()) addUnique(merged,seen,postRepo.findConnectionsPosts(friendsInGroup));
            addUnique(merged,seen,postRepo.findPostsVisibleToGroups(List.of(groupId)));
        }

        merged.sort(Comparator.comparing(PostEntity::getCreatedAt).reversed());
        size = Math.min(size,50);
        long total = merged.size();
        int from = Math.min(page*size,merged.size()), to = Math.min(from+size,merged.size());
        List<PostDto.PostResponse> content = merged.subList(from,to).stream().map(postService::toResponse).toList();
        return PagedResponse.of(content,page,size,total);
    }
    private void addUnique(List<PostEntity> t, Set<Long> s, List<PostEntity> src) { for (PostEntity p:src) if(s.add(p.getId())) t.add(p); }
}