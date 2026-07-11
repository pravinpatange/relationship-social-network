package io.relationship.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PostVisibilityGroupRepository extends JpaRepository<PostVisibilityGroupEntity,Long> {
    List<PostVisibilityGroupEntity> findAllByPostId(Long postId);
    void deleteAllByPostId(Long postId);
}