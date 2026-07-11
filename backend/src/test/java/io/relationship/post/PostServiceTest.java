package io.relationship.post;
import io.relationship.common.exception.*;
import io.relationship.friend.*;
import io.relationship.group.*;
import io.relationship.post.PostEntity.VisibilityType;
import io.relationship.user.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock PostRepository postRepo; @Mock PostVisibilityGroupRepository pvgRepo;
    @Mock GroupRepository groupRepo; @Mock UserService userService;
    @Mock FriendRepository friendRepo; @Mock FriendGroupMappingRepository mappingRepo;
    @InjectMocks PostService postService;
    private UserEntity author, viewer;
    private PostEntity publicPost, privatePost, allConnPost, selGroupPost;
    @BeforeEach void setup() {
        author = UserEntity.builder().id(1L).username("pravin").build();
        viewer = UserEntity.builder().id(2L).username("amit").build();
        publicPost  = PostEntity.builder().id(100L).author(author).visibilityType(VisibilityType.PUBLIC).build();
        privatePost = PostEntity.builder().id(101L).author(author).visibilityType(VisibilityType.PRIVATE).build();
        allConnPost = PostEntity.builder().id(102L).author(author).visibilityType(VisibilityType.ALL_CONNECTIONS).build();
        selGroupPost= PostEntity.builder().id(103L).author(author).visibilityType(VisibilityType.SELECTED_GROUPS).build();
    }
    @Test void publicPost_visibleToAll() {
        when(postRepo.findById(100L)).thenReturn(Optional.of(publicPost));
        assertThat(postService.getPostById(100L,2L)).isNotNull();
    }
    @Test void privatePost_authorCanSee() {
        when(postRepo.findById(101L)).thenReturn(Optional.of(privatePost));
        assertThat(postService.getPostById(101L,1L)).isNotNull();
    }
    @Test void privatePost_strangerCannotSee() {
        when(postRepo.findById(101L)).thenReturn(Optional.of(privatePost));
        assertThatThrownBy(()->postService.getPostById(101L,2L)).isInstanceOf(ForbiddenException.class);
    }
    @Test void allConnPost_friendCanSee() {
        when(postRepo.findById(102L)).thenReturn(Optional.of(allConnPost));
        when(friendRepo.areFriends(1L,2L)).thenReturn(true);
        assertThat(postService.getPostById(102L,2L)).isNotNull();
    }
    @Test void allConnPost_strangerCannotSee() {
        when(postRepo.findById(102L)).thenReturn(Optional.of(allConnPost));
        when(friendRepo.areFriends(1L,2L)).thenReturn(false);
        assertThatThrownBy(()->postService.getPostById(102L,2L)).isInstanceOf(ForbiddenException.class);
    }
    @Test void selectedGroups_memberCanSee() {
        when(postRepo.findById(103L)).thenReturn(Optional.of(selGroupPost));
        var grp = mock(UserGroupEntity.class); when(grp.getId()).thenReturn(10L);
        var pvg = mock(PostVisibilityGroupEntity.class); when(pvg.getGroup()).thenReturn(grp);
        when(pvgRepo.findAllByPostId(103L)).thenReturn(List.of(pvg));
        var mapping = mock(FriendGroupMappingEntity.class); var vGrp = mock(UserGroupEntity.class);
        when(vGrp.getId()).thenReturn(10L); when(mapping.getGroup()).thenReturn(vGrp);
        when(mappingRepo.findAllByOwnerIdAndFriendId(1L,2L)).thenReturn(List.of(mapping));
        assertThat(postService.getPostById(103L,2L)).isNotNull();
    }
    @Test void selectedGroups_nonMemberCannotSee() {
        when(postRepo.findById(103L)).thenReturn(Optional.of(selGroupPost));
        var grp = mock(UserGroupEntity.class); when(grp.getId()).thenReturn(10L);
        var pvg = mock(PostVisibilityGroupEntity.class); when(pvg.getGroup()).thenReturn(grp);
        when(pvgRepo.findAllByPostId(103L)).thenReturn(List.of(pvg));
        when(mappingRepo.findAllByOwnerIdAndFriendId(1L,2L)).thenReturn(List.of());
        assertThatThrownBy(()->postService.getPostById(103L,2L)).isInstanceOf(ForbiddenException.class);
    }
    @Test void deletePost_notAuthor_throws() {
        when(postRepo.findById(100L)).thenReturn(Optional.of(publicPost));
        assertThatThrownBy(()->postService.deletePost(100L,2L)).isInstanceOf(ForbiddenException.class);
    }
    @Test void createPost_selectedGroupsWithoutGroupIds_throws() {
        when(userService.getById(1L)).thenReturn(author);
        assertThatThrownBy(()->postService.createPost(1L,new PostDto.CreatePostRequest(null,null,VisibilityType.SELECTED_GROUPS,null)))
            .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("SELECTED_GROUPS");
    }
}