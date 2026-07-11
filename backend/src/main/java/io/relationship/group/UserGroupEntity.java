package io.relationship.group;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="user_groups") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserGroupEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="owner_user_id",nullable=false) private UserEntity owner;
    @Column(name="group_name",nullable=false,length=100) private String groupName;
    @Lob private String description;
    @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
}