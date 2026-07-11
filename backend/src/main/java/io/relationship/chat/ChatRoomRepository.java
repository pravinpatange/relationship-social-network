package io.relationship.chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity,Long> {
    @Query("""
        SELECT r FROM ChatRoomEntity r
        WHERE (r.user1.id=:u1 AND r.user2.id=:u2 OR r.user1.id=:u2 AND r.user2.id=:u1)
          AND (:gid IS NULL AND r.contextGroup IS NULL OR r.contextGroup.id=:gid)
    """)
    Optional<ChatRoomEntity> findExistingRoom(@Param("u1")Long u1,@Param("u2")Long u2,@Param("gid")Long gid);

    @Query("SELECT r FROM ChatRoomEntity r WHERE r.user1.id=:uid OR r.user2.id=:uid ORDER BY r.createdAt DESC")
    List<ChatRoomEntity> findRoomsForUser(@Param("uid")Long uid);
}