package io.relationship.friend;
import io.relationship.group.UserGroupEntity;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="friend_group_mapping") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FriendGroupMappingEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="owner_user_id",nullable=false) private UserEntity owner;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="friend_user_id",nullable=false) private UserEntity friend;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="group_id",nullable=false) private UserGroupEntity group;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
}