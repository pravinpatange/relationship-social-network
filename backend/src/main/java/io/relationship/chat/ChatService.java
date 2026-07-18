package io.relationship.chat;
import io.relationship.common.exception.*;
import io.relationship.group.*;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository roomRepo;
    private final ChatMessageRepository msgRepo;
    private final UserService userService;
    private final GroupRepository groupRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChatRoomEntity getOrCreateRoom(Long uid, Long otherId, Long groupId) {
        return roomRepo.findExistingRoom(uid,otherId,groupId).orElseGet(()->{
            UserGroupEntity grp = groupId!=null ? groupRepo.findById(groupId).orElseThrow(()->new ResourceNotFoundException("Group",groupId)) : null;
            return roomRepo.save(ChatRoomEntity.builder().user1(userService.getById(uid)).user2(userService.getById(otherId)).contextGroup(grp).build());
        });
    }
    @Transactional
    public ChatMessageEntity sendMessage(Long uid, Long roomId, String text) {
        ChatRoomEntity room = roomRepo.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Chat room",roomId));
        if (!room.getUser1().getId().equals(uid) && !room.getUser2().getId().equals(uid))
            throw new ForbiddenException("You are not a participant of this chat room");
        
        ChatMessageEntity savedMsg = msgRepo.save(ChatMessageEntity.builder().room(room).sender(userService.getById(uid)).message(text).build());
        
        Long receiverId = room.getUser1().getId().equals(uid) ? room.getUser2().getId() : room.getUser1().getId();
        messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/messages", toMsgResponse(savedMsg));
        
        return savedMsg;
    }
    @Transactional(readOnly=true)
    public List<ChatMessageEntity> getMessages(Long uid, Long roomId) {
        ChatRoomEntity room = roomRepo.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Chat room",roomId));
        if (!room.getUser1().getId().equals(uid) && !room.getUser2().getId().equals(uid))
            throw new ForbiddenException("Access denied to this chat room");
        return msgRepo.findAllByRoomIdOrderByCreatedAtAsc(roomId);
    }
    @Transactional(readOnly=true) public List<ChatRoomEntity> getMyRooms(Long uid) { return roomRepo.findRoomsForUser(uid); }
    public ChatDto.RoomResponse toRoomResponse(ChatRoomEntity r) {
        return ChatDto.RoomResponse.builder().id(r.getId()).user1Id(r.getUser1().getId()).user1Username(r.getUser1().getUsername())
            .user2Id(r.getUser2().getId()).user2Username(r.getUser2().getUsername())
            .contextGroupId(r.getContextGroup()!=null?r.getContextGroup().getId():null)
            .contextGroupName(r.getContextGroup()!=null?r.getContextGroup().getGroupName():null)
            .createdAt(r.getCreatedAt()).build();
    }
    public ChatDto.MessageResponse toMsgResponse(ChatMessageEntity m) {
        return ChatDto.MessageResponse.builder().id(m.getId()).roomId(m.getRoom().getId())
            .senderId(m.getSender().getId()).senderUsername(m.getSender().getUsername())
            .message(m.getMessage()).createdAt(m.getCreatedAt()).build();
    }
}