package io.relationship.notification;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repo;
    private final UserService userService;
    @Transactional
    public void createNotification(Long recipientId, Long actorId, NotificationEntity.NotificationType type, Long refId) {
        if (recipientId==null || recipientId.equals(actorId)) return;
        repo.save(NotificationEntity.builder().recipient(userService.getById(recipientId)).actor(userService.getById(actorId)).type(type).referenceId(refId).isRead(false).build());
    }
    @Transactional(readOnly=true) public List<NotificationDto.NotificationResponse> getMyNotifications(Long uid) { return repo.findAllByRecipientIdOrderByCreatedAtDesc(uid).stream().map(this::toResponse).toList(); }
    @Transactional(readOnly=true) public long getUnreadCount(Long uid) { return repo.countUnread(uid); }
    @Transactional public void markAllRead(Long uid) { repo.markAllAsRead(uid); }
    @Transactional public void markOneRead(Long id, Long uid) { repo.markOneAsRead(id,uid); }
    public NotificationDto.NotificationResponse toResponse(NotificationEntity n) {
        return NotificationDto.NotificationResponse.builder().id(n.getId()).recipientId(n.getRecipient().getId())
            .actorId(n.getActor()!=null?n.getActor().getId():null).actorUsername(n.getActor()!=null?n.getActor().getUsername():null)
            .type(n.getType()).referenceId(n.getReferenceId()).read(n.isRead()).createdAt(n.getCreatedAt()).build();
    }
}