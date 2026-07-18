package io.relationship.post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {
    Page<PostEntity> findAllByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    @Query("SELECT p FROM PostEntity p WHERE p.visibilityType = :pub ORDER BY p.createdAt DESC")
    List<PostEntity> findAllPublicPosts(@Param("pub") PostEntity.VisibilityType pub);

    @Query("SELECT p FROM PostEntity p WHERE p.author.id IN :ids AND p.visibilityType IN :types ORDER BY p.createdAt DESC")
    List<PostEntity> findConnectionsPosts(@Param("ids") List<Long> ids, @Param("types") List<PostEntity.VisibilityType> types);

    @Query("SELECT p FROM PostEntity p JOIN PostVisibilityGroupEntity pvg ON pvg.post.id=p.id WHERE pvg.group.id IN :gids AND p.visibilityType = :sel ORDER BY p.createdAt DESC")
    List<PostEntity> findPostsVisibleToGroups(@Param("gids") List<Long> gids, @Param("sel") PostEntity.VisibilityType sel);
}