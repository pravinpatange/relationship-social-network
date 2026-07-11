package io.relationship.notification;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="notifications") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="recipient_id",nullable=false) private UserEntity recipient;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="actor_id") private UserEntity actor;
    @Column(nullable=false,length=30) @Enumerated(EnumType.STRING) private NotificationType type;
    @Column(name="reference_id") private Long referenceId;
    @Column(name="is_read") @Builder.Default private boolean isRead=false;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    public enum NotificationType { FRIEND_REQUEST,FRIEND_ACCEPTED,POST_LIKE,POST_COMMENT,FOLLOW }
}