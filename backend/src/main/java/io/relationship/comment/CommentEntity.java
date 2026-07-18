package io.relationship.comment;
import io.relationship.post.PostEntity;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="comments") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="post_id",nullable=false) private PostEntity post;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private UserEntity author;
    @Column(name="comment_text",nullable=false, columnDefinition="TEXT") private String commentText;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
}