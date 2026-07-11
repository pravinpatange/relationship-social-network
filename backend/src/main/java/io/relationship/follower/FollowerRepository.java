package io.relationship.follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface FollowerRepository extends JpaRepository<FollowerEntity,Long> {
    Optional<FollowerEntity> findByFollowerIdAndFollowingId(Long f,Long g);
    boolean existsByFollowerIdAndFollowingId(Long f,Long g);
    List<FollowerEntity> findAllByFollowingId(Long uid);
    List<FollowerEntity> findAllByFollowerId(Long uid);
}