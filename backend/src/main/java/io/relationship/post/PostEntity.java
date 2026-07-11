package io.relationship.post;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="posts") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PostEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private UserEntity author;
    @Lob private String caption;
    @Column(name="media_url") private String mediaUrl;
    @Column(name="visibility_type",nullable=false,length=30) @Enumerated(EnumType.STRING) @Builder.Default
    private VisibilityType visibilityType = VisibilityType.ALL_CONNECTIONS;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
    public enum VisibilityType { PRIVATE, SELECTED_GROUPS, ALL_CONNECTIONS, PUBLIC }
}