package io.relationship.chat;
import io.relationship.group.UserGroupEntity;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="chat_rooms") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRoomEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user1_id",nullable=false) private UserEntity user1;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user2_id",nullable=false) private UserEntity user2;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="context_group_id") private UserGroupEntity contextGroup;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
}