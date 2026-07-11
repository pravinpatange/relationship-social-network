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
    @Query("SELECT p FROM PostEntity p WHERE p.visibilityType='PUBLIC' ORDER BY p.createdAt DESC")
    List<PostEntity> findAllPublicPosts();
    @Query("SELECT p FROM PostEntity p WHERE p.author.id IN :ids AND p.visibilityType IN ('ALL_CONNECTIONS','PUBLIC') ORDER BY p.createdAt DESC")
    List<PostEntity> findConnectionsPosts(@Param("ids")List<Long> ids);
    @Query("SELECT p FROM PostEntity p JOIN PostVisibilityGroupEntity pvg ON pvg.post.id=p.id WHERE pvg.group.id IN :gids AND p.visibilityType='SELECTED_GROUPS' ORDER BY p.createdAt DESC")
    List<PostEntity> findPostsVisibleToGroups(@Param("gids")List<Long> gids);
}