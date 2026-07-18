package io.relationship.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    /**
     * Search users by username or display name (case-insensitive).
     * Excludes the searching user from results.
     */
    @Query("""
        SELECT u FROM UserEntity u
        WHERE u.id <> :excludeId
          AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
           OR  LOWER(u.displayName) LIKE LOWER(CONCAT('%', :query, '%')))
          AND u.isActive = true
    """)
    Page<UserEntity> searchUsers(@Param("query") String query,
                                 @Param("excludeId") Long excludeId,
                                 Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.id <> :excludeId AND u.isActive = true ORDER BY u.createdAt DESC")
    Page<UserEntity> findSuggestions(@Param("excludeId") Long excludeId, Pageable pageable);
}
