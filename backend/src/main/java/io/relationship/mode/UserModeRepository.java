package io.relationship.mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UserModeRepository extends JpaRepository<UserModeEntity,Long> {
    Optional<UserModeEntity> findByUserId(Long userId);
}