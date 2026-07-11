package io.relationship.group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface GroupRepository extends JpaRepository<UserGroupEntity,Long> {
    List<UserGroupEntity> findAllByOwnerId(Long ownerId);
    Optional<UserGroupEntity> findByIdAndOwnerId(Long id, Long ownerId);
    boolean existsByOwnerIdAndGroupNameIgnoreCase(Long ownerId, String name);
}