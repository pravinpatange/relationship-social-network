package io.relationship.friend;
import io.relationship.common.exception.*;
import io.relationship.friend.FriendshipEntity.FriendshipStatus;
import io.relationship.group.GroupRepository;
import io.relationship.notification.NotificationService;
import io.relationship.user.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FriendServiceTest {
    @Mock FriendRepository friendRepo; @Mock FriendGroupMappingRepository mappingRepo;
    @Mock UserService userService; @Mock GroupRepository groupRepo; @Mock NotificationService notifService;
    @InjectMocks FriendService friendService;
    private UserEntity userA, userB;
    @BeforeEach void setup() {
        userA = UserEntity.builder().id(1L).username("pravin").build();
        userB = UserEntity.builder().id(2L).username("amit").build();
    }
    @Test void sendRequest_success() {
        when(userService.getById(2L)).thenReturn(userB);
        when(friendRepo.findByRequesterIdAndReceiverId(1L,2L)).thenReturn(Optional.empty());
        when(friendRepo.findByRequesterIdAndReceiverId(2L,1L)).thenReturn(Optional.empty());
        when(userService.getById(1L)).thenReturn(userA);
        var saved = FriendshipEntity.builder().id(10L).requester(userA).receiver(userB).status(FriendshipStatus.PENDING).build();
        when(friendRepo.save(any())).thenReturn(saved);
        var result = friendService.sendRequest(1L,2L);
        assertThat(result.getStatus()).isEqualTo(FriendshipStatus.PENDING);
        verify(notifService).createNotification(eq(2L),eq(1L),any(),anyLong());
    }
    @Test void sendRequest_toSelf_throws() { assertThatThrownBy(()->friendService.sendRequest(1L,1L)).isInstanceOf(IllegalArgumentException.class); }
    @Test void sendRequest_duplicate_throws() {
        when(userService.getById(2L)).thenReturn(userB);
        when(friendRepo.findByRequesterIdAndReceiverId(1L,2L)).thenReturn(Optional.of(FriendshipEntity.builder().build()));
        assertThatThrownBy(()->friendService.sendRequest(1L,2L)).isInstanceOf(DuplicateResourceException.class);
    }
    @Test void acceptRequest_wrongUser_throws() {
        var f = FriendshipEntity.builder().id(10L).requester(userA).receiver(userB).status(FriendshipStatus.PENDING).build();
        when(friendRepo.findById(10L)).thenReturn(Optional.of(f));
        assertThatThrownBy(()->friendService.acceptRequest(10L,1L)).isInstanceOf(ForbiddenException.class);
    }
    @Test void acceptRequest_success() {
        var f = FriendshipEntity.builder().id(10L).requester(userA).receiver(userB).status(FriendshipStatus.PENDING).build();
        when(friendRepo.findById(10L)).thenReturn(Optional.of(f));
        when(friendRepo.save(any())).thenAnswer(i->i.getArgument(0));
        var result = friendService.acceptRequest(10L,2L);
        assertThat(result.getStatus()).isEqualTo(FriendshipStatus.ACCEPTED);
    }
    @Test void unfriend_success() {
        var f = FriendshipEntity.builder().id(10L).requester(userA).receiver(userB).status(FriendshipStatus.ACCEPTED).build();
        when(friendRepo.findBetweenUsers(1L,2L)).thenReturn(Optional.of(f));
        friendService.unfriend(1L,2L);
        verify(mappingRepo).deleteByOwnerIdAndFriendId(1L,2L); verify(mappingRepo).deleteByOwnerIdAndFriendId(2L,1L);
        verify(friendRepo).delete(f);
    }
    @Test void blockUser_existingFriendship_setsBlocked() {
        when(userService.getById(2L)).thenReturn(userB);
        var f = FriendshipEntity.builder().id(10L).requester(userA).receiver(userB).status(FriendshipStatus.ACCEPTED).build();
        when(friendRepo.findBetweenUsers(1L,2L)).thenReturn(Optional.of(f));
        when(friendRepo.save(any())).thenAnswer(i->i.getArgument(0));
        var result = friendService.blockUser(1L,2L);
        assertThat(result.getStatus()).isEqualTo(FriendshipStatus.BLOCKED);
    }
}