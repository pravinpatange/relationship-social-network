package io.relationship.friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FriendGroupMappingRepository extends JpaRepository<FriendGroupMappingEntity,Long> {
    List<FriendGroupMappingEntity> findAllByOwnerIdAndFriendId(Long o,Long f);
    boolean existsByOwnerIdAndFriendIdAndGroupId(Long o,Long f,Long g);
    List<FriendGroupMappingEntity> findAllByOwnerIdAndGroupId(Long o,Long g);
    @Query("SELECT DISTINCT m.group.id FROM FriendGroupMappingEntity m WHERE m.friend.id=:uid")
    List<Long> findGroupsWhereUserIsMember(@Param("uid")Long uid);
    @Modifying @Query("DELETE FROM FriendGroupMappingEntity m WHERE m.owner.id=:o AND m.friend.id=:f")
    void deleteByOwnerIdAndFriendId(@Param("o")Long o,@Param("f")Long f);
}