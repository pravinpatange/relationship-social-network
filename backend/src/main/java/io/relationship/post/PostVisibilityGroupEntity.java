package io.relationship.post;
import io.relationship.group.UserGroupEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="post_visibility_groups") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PostVisibilityGroupEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="post_id",nullable=false) private PostEntity post;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="group_id",nullable=false) private UserGroupEntity group;
}