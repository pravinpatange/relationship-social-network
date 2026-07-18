package io.relationship.chat;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="chat_messages") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="room_id",nullable=false) private ChatRoomEntity room;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="sender_id",nullable=false) private UserEntity sender;
    @Column(nullable=false, columnDefinition="TEXT") private String message;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
}