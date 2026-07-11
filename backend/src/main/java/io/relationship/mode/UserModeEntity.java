package io.relationship.mode;
import io.relationship.group.UserGroupEntity;
import io.relationship.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
@Entity @Table(name="user_modes") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserModeEntity {
    @Id @Column(name="user_id") private Long userId;
    @OneToOne(fetch=FetchType.LAZY) @MapsId @JoinColumn(name="user_id") private UserEntity user;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="active_group_id") private UserGroupEntity activeGroup;
    @Column(name="active_mode",nullable=false,length=30) @Builder.Default private String activeMode="ALL";
    @UpdateTimestamp @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
}