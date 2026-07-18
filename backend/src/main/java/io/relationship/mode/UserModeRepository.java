package io.relationship.mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface UserModeRepository extends JpaRepository<UserModeEntity,Long> {
    @EntityGraph(attributePaths = {"activeGroup"})
    Optional<UserModeEntity> findByUserId(Long userId);
}