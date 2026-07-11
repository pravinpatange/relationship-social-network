package io.relationship.follower;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="followers") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FollowerEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="follower_user_id",nullable=false) private UserEntity follower;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="following_user_id",nullable=false) private UserEntity following;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
}