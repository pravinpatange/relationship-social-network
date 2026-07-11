package io.relationship.notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
    List<NotificationEntity> findAllByRecipientIdOrderByCreatedAtDesc(Long uid);
    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.recipient.id=:uid AND n.isRead=false")
    long countUnread(@Param("uid")Long uid);
    @Modifying @Query("UPDATE NotificationEntity n SET n.isRead=true WHERE n.recipient.id=:uid")
    void markAllAsRead(@Param("uid")Long uid);
    @Modifying @Query("UPDATE NotificationEntity n SET n.isRead=true WHERE n.id=:id AND n.recipient.id=:uid")
    void markOneAsRead(@Param("id")Long id,@Param("uid")Long uid);
}