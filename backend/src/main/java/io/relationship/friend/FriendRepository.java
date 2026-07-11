package io.relationship.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface FriendRepository extends JpaRepository<FriendshipEntity, Long> {
    Optional<FriendshipEntity> findByRequesterIdAndReceiverId(Long r, Long v);

    @Query("SELECT f FROM FriendshipEntity f WHERE (f.requester.id=:uid OR f.receiver.id=:uid) AND f.status=:s")
    List<FriendshipEntity> findAllByUserIdAndStatus(@Param("uid") Long uid, @Param("s") FriendshipEntity.FriendshipStatus s);

    @Query("SELECT f FROM FriendshipEntity f WHERE f.receiver.id=:uid AND f.status='PENDING'")
    List<FriendshipEntity> findPendingRequestsForUser(@Param("uid") Long uid);

    @Query("SELECT CASE WHEN COUNT(f)>0 THEN TRUE ELSE FALSE END FROM FriendshipEntity f WHERE ((f.requester.id=:u1 AND f.receiver.id=:u2) OR (f.requester.id=:u2 AND f.receiver.id=:u1)) AND f.status='ACCEPTED'")
    boolean areFriends(@Param("u1") Long u1, @Param("u2") Long u2);

    @Query("SELECT f FROM FriendshipEntity f WHERE (f.requester.id=:u1 AND f.receiver.id=:u2) OR (f.requester.id=:u2 AND f.receiver.id=:u1)")
    Optional<FriendshipEntity> findBetweenUsers(@Param("u1") Long u1, @Param("u2") Long u2);
}
