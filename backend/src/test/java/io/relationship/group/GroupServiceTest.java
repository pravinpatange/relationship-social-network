package io.relationship.group;
import io.relationship.common.exception.*;
import io.relationship.user.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock GroupRepository groupRepo; @Mock UserService userService;
    @InjectMocks GroupService groupService;
    private UserEntity owner;
    @BeforeEach void setup() { owner = UserEntity.builder().id(1L).username("pravin").build(); }

    @Test void createGroup_success() {
        when(userService.getById(1L)).thenReturn(owner);
        when(groupRepo.existsByOwnerIdAndGroupNameIgnoreCase(1L,"Family")).thenReturn(false);
        when(groupRepo.save(any())).thenReturn(UserGroupEntity.builder().id(10L).owner(owner).groupName("Family").build());
        var g = groupService.createGroup(1L,new GroupDto.CreateGroupRequest("Family","desc"));
        assertThat(g.getGroupName()).isEqualTo("Family");
    }
    @Test void createGroup_duplicate_throws() {
        when(userService.getById(1L)).thenReturn(owner);
        when(groupRepo.existsByOwnerIdAndGroupNameIgnoreCase(1L,"Family")).thenReturn(true);
        assertThatThrownBy(()->groupService.createGroup(1L,new GroupDto.CreateGroupRequest("Family","desc")))
            .isInstanceOf(DuplicateResourceException.class).hasMessageContaining("Family");
    }
    @Test void deleteGroup_notOwner_throws() {
        var other = UserEntity.builder().id(99L).build();
        var g = UserGroupEntity.builder().id(10L).owner(other).groupName("X").build();
        when(groupRepo.findById(10L)).thenReturn(Optional.of(g));
        assertThatThrownBy(()->groupService.deleteGroup(10L,1L)).isInstanceOf(ForbiddenException.class);
    }
    @Test void getGroupById_notFound_throws() {
        when(groupRepo.findByIdAndOwnerId(99L,1L)).thenReturn(Optional.empty());
        assertThatThrownBy(()->groupService.getGroupById(99L,1L)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test void updateGroup_sameNameNoConflict() {
        var g = UserGroupEntity.builder().id(10L).owner(owner).groupName("Family").build();
        when(groupRepo.findByIdAndOwnerId(10L,1L)).thenReturn(Optional.of(g));
        when(groupRepo.save(any())).thenAnswer(i->i.getArgument(0));
        var updated = groupService.updateGroup(10L,1L,new GroupDto.UpdateGroupRequest("Family","new desc"));
        assertThat(updated.getGroupName()).isEqualTo("Family");
    }
}