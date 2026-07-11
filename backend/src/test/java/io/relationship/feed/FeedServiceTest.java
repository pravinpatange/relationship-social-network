package io.relationship.feed;
import io.relationship.friend.*;
import io.relationship.friend.FriendshipEntity.FriendshipStatus;
import io.relationship.post.*;
import io.relationship.user.UserEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FeedServiceTest {
    @Mock PostRepository postRepo; @Mock FriendRepository friendRepo;
    @Mock FriendGroupMappingRepository mappingRepo; @Mock PostService postService;
    @InjectMocks FeedService feedService;
    private UserEntity author;
    @BeforeEach void setup() { author = UserEntity.builder().id(1L).username("pravin").build(); }

    private PostEntity makePost(Long id, PostEntity.VisibilityType type) {
        return PostEntity.builder().id(id).author(author).visibilityType(type).createdAt(LocalDateTime.now()).build();
    }
    @Test void allMode_returnsPublicPosts() {
        var pub = makePost(1L, PostEntity.VisibilityType.PUBLIC);
        when(friendRepo.findAllByUserIdAndStatus(eq(10L),any())).thenReturn(List.of());
        when(postRepo.findAllPublicPosts()).thenReturn(List.of(pub));
        when(mappingRepo.findGroupsWhereUserIsMember(10L)).thenReturn(List.of());
        when(postService.toResponse(any())).thenReturn(new PostDto.PostResponse());
        var result = feedService.getFeed(10L,null,0,20);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
    @Test void allMode_deduplicatesPublicAndConnectionPosts() {
        var pub = makePost(1L, PostEntity.VisibilityType.PUBLIC);
        var conn = makePost(2L, PostEntity.VisibilityType.ALL_CONNECTIONS);
        UserEntity friend = UserEntity.builder().id(2L).build();
        var friendship = FriendshipEntity.builder().id(5L).requester(UserEntity.builder().id(10L).build()).receiver(friend).status(FriendshipStatus.ACCEPTED).build();
        when(friendRepo.findAllByUserIdAndStatus(eq(10L),any())).thenReturn(List.of(friendship));
        when(postRepo.findAllPublicPosts()).thenReturn(List.of(pub));
        when(postRepo.findConnectionsPosts(any())).thenReturn(List.of(pub,conn));
        when(mappingRepo.findGroupsWhereUserIsMember(10L)).thenReturn(List.of());
        when(postService.toResponse(any())).thenReturn(new PostDto.PostResponse());
        var result = feedService.getFeed(10L,null,0,20);
        // pub should not be duplicated - total should be 2
        assertThat(result.getTotalElements()).isEqualTo(2);
    }
    @Test void groupMode_onlyShowsGroupAndPublicPosts() {
        var pub = makePost(1L, PostEntity.VisibilityType.PUBLIC);
        var grpPost = makePost(2L, PostEntity.VisibilityType.SELECTED_GROUPS);
        when(friendRepo.findAllByUserIdAndStatus(eq(10L),any())).thenReturn(List.of());
        when(postRepo.findAllPublicPosts()).thenReturn(List.of(pub));
        when(mappingRepo.findAllByOwnerIdAndGroupId(10L,5L)).thenReturn(List.of());
        when(postRepo.findPostsVisibleToGroups(List.of(5L))).thenReturn(List.of(grpPost));
        when(postService.toResponse(any())).thenReturn(new PostDto.PostResponse());
        var result = feedService.getFeed(10L,5L,0,20);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }
    @Test void pagination_appliedCorrectly() {
        var posts = new ArrayList<PostEntity>();
        for (int i=0;i<25;i++) posts.add(makePost((long)i,PostEntity.VisibilityType.PUBLIC));
        when(friendRepo.findAllByUserIdAndStatus(eq(10L),any())).thenReturn(List.of());
        when(postRepo.findAllPublicPosts()).thenReturn(posts);
        when(mappingRepo.findGroupsWhereUserIsMember(10L)).thenReturn(List.of());
        when(postService.toResponse(any())).thenReturn(new PostDto.PostResponse());
        var page0 = feedService.getFeed(10L,null,0,10);
        assertThat(page0.getContent()).hasSize(10);
        assertThat(page0.getTotalElements()).isEqualTo(25);
        assertThat(page0.getTotalPages()).isEqualTo(3);
        assertThat(page0.isLast()).isFalse();
        var page2 = feedService.getFeed(10L,null,2,10);
        assertThat(page2.getContent()).hasSize(5);
        assertThat(page2.isLast()).isTrue();
    }
}