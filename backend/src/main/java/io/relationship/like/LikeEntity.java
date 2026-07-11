package io.relationship.like;
import io.relationship.post.PostEntity;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="likes") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LikeEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="post_id",nullable=false) private PostEntity post;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private UserEntity user;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
}