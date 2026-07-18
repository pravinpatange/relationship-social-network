package io.relationship.friend;
import io.relationship.common.exception.*;
import io.relationship.friend.FriendshipEntity.FriendshipStatus;
import io.relationship.group.*;
import io.relationship.notification.*;
import io.relationship.notification.NotificationEntity.NotificationType;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
@Service @RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepo;
    private final FriendGroupMappingRepository mappingRepo;
    private final UserService userService;
    private final GroupRepository groupRepo;
    private final NotificationService notifService;

    @Transactional
    public FriendshipEntity sendRequest(Long reqId, Long recId) {
        if (reqId.equals(recId)) throw new IllegalArgumentException("Cannot send friend request to yourself");
        userService.getById(recId);
        
        Optional<FriendshipEntity> existing = friendRepo.findBetweenUsers(reqId, recId);
        if (existing.isPresent()) {
            FriendshipEntity f = existing.get();
            if (f.getStatus() == FriendshipStatus.PENDING) {
                if (f.getRequester().getId().equals(reqId)) throw new DuplicateResourceException("Friend request already sent");
                else throw new DuplicateResourceException("This user already sent you a request");
            }
            if (f.getStatus() == FriendshipStatus.ACCEPTED) {
                throw new DuplicateResourceException("You are already friends");
            }
            if (f.getStatus() == FriendshipStatus.BLOCKED) {
                throw new ForbiddenException("Action not allowed");
            }
            // If REJECTED, we reset to PENDING and update requester/receiver
            if (f.getStatus() == FriendshipStatus.REJECTED) {
                f.setRequester(userService.getById(reqId));
                f.setReceiver(userService.getById(recId));
                f.setStatus(FriendshipStatus.PENDING);
                notifService.createNotification(recId, reqId, NotificationType.FRIEND_REQUEST, f.getId());
                return friendRepo.save(f);
            }
        }

        FriendshipEntity f = friendRepo.save(FriendshipEntity.builder().requester(userService.getById(reqId)).receiver(userService.getById(recId)).status(FriendshipStatus.PENDING).build());
        notifService.createNotification(recId,reqId,NotificationType.FRIEND_REQUEST,f.getId());
        return f;
    }
    @Transactional
    public FriendshipEntity acceptRequest(Long fId, Long uid) {
        FriendshipEntity f = getById(fId);
        if (!f.getReceiver().getId().equals(uid)) throw new ForbiddenException("You cannot accept this request");
        if (f.getStatus()!=FriendshipStatus.PENDING) throw new IllegalArgumentException("Request is not PENDING");
        f.setStatus(FriendshipStatus.ACCEPTED); f = friendRepo.save(f);
        notifService.createNotification(f.getRequester().getId(),uid,NotificationType.FRIEND_ACCEPTED,f.getId());
        return f;
    }
    @Transactional
    public FriendshipEntity rejectRequest(Long fId, Long uid) {
        FriendshipEntity f = getById(fId);
        if (!f.getReceiver().getId().equals(uid)) throw new ForbiddenException("You cannot reject this request");
        f.setStatus(FriendshipStatus.REJECTED); return friendRepo.save(f);
    }
    @Transactional
    public void unfriend(Long uid, Long fid) {
        FriendshipEntity f = friendRepo.findBetweenUsers(uid,fid).orElseThrow(()->new ResourceNotFoundException("Friendship not found"));
        if (f.getStatus()!=FriendshipStatus.ACCEPTED) throw new IllegalArgumentException("You are not friends with this user");
        mappingRepo.deleteByOwnerIdAndFriendId(uid,fid); mappingRepo.deleteByOwnerIdAndFriendId(fid,uid);
        friendRepo.delete(f);
    }
    @Transactional
    public FriendshipEntity blockUser(Long blockerId, Long targetId) {
        if (blockerId.equals(targetId)) throw new IllegalArgumentException("Cannot block yourself");
        userService.getById(targetId);
        return friendRepo.findBetweenUsers(blockerId,targetId).map(f->{
            f.setStatus(FriendshipStatus.BLOCKED);
            mappingRepo.deleteByOwnerIdAndFriendId(blockerId,targetId); mappingRepo.deleteByOwnerIdAndFriendId(targetId,blockerId);
            return friendRepo.save(f);
        }).orElseGet(()->{
            return friendRepo.save(FriendshipEntity.builder().requester(userService.getById(blockerId)).receiver(userService.getById(targetId)).status(FriendshipStatus.BLOCKED).build());
        });
    }
    @Transactional
    public void unblockUser(Long blockerId, Long targetId) {
        FriendshipEntity f = friendRepo.findBetweenUsers(blockerId,targetId).orElseThrow(()->new ResourceNotFoundException("No relationship found"));
        if (f.getStatus()!=FriendshipStatus.BLOCKED) throw new IllegalArgumentException("User is not blocked");
        friendRepo.delete(f);
    }
    @Transactional(readOnly=true) public List<FriendshipEntity> getAcceptedFriends(Long uid) { return friendRepo.findAllByUserIdAndStatus(uid,FriendshipStatus.ACCEPTED); }
    @Transactional(readOnly=true) public List<FriendshipEntity> getPendingRequests(Long uid) { return friendRepo.findPendingRequestsForUser(uid); }
    @Transactional(readOnly=true) public List<FriendshipEntity> getSentRequests(Long uid) { return friendRepo.findSentRequestsForUser(uid); }
    @Transactional
    public void assignFriendToGroups(Long ownerId, Long friendId, List<Long> groupIds) {
        if (!friendRepo.areFriends(ownerId,friendId)) throw new ForbiddenException("You can only assign groups to accepted friends");
        UserEntity owner=userService.getById(ownerId); UserEntity friend=userService.getById(friendId);
        List<FriendGroupMappingEntity> existing = mappingRepo.findAllByOwnerIdAndFriendId(ownerId, friendId);
        for (FriendGroupMappingEntity m : existing) {
            if (!groupIds.contains(m.getGroup().getId())) {
                mappingRepo.delete(m);
            }
        }
        for (Long gid : groupIds) {
            if (existing.stream().noneMatch(m -> m.getGroup().getId().equals(gid))) {
                UserGroupEntity g = groupRepo.findById(gid).orElseThrow(()->new ResourceNotFoundException("Group",gid));
                if (!g.getOwner().getId().equals(ownerId)) throw new ForbiddenException("Group "+gid+" does not belong to you");
                mappingRepo.save(FriendGroupMappingEntity.builder().owner(owner).friend(friend).group(g).build());
            }
        }
    }
    @Transactional(readOnly=true)
    public List<FriendDto.FriendGroupResponse> getFriendGroups(Long ownerId, Long friendId) {
        return mappingRepo.findAllByOwnerIdAndFriendId(ownerId,friendId).stream()
            .map(m->FriendDto.FriendGroupResponse.builder().groupId(m.getGroup().getId()).groupName(m.getGroup().getGroupName()).build()).toList();
    }
    private FriendshipEntity getById(Long id) { return friendRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Friend request",id)); }
    public FriendDto.FriendshipResponse toResponse(FriendshipEntity f) {
        return FriendDto.FriendshipResponse.builder().id(f.getId()).requesterId(f.getRequester().getId()).requesterUsername(f.getRequester().getUsername())
            .receiverId(f.getReceiver().getId()).receiverUsername(f.getReceiver().getUsername()).status(f.getStatus().name()).createdAt(f.getCreatedAt()).build();
    }
}