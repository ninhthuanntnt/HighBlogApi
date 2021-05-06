package com.high.highblog.repository;

import com.high.highblog.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findBySourceId(Long sourceId);

    @Query("SELECT n FROM Notification n"
        + " JOIN UserNotification un ON un.notificationId = n.id"
        + " WHERE un.userId = :userId AND un.seen = :seen")
    List<Notification> findByUserIdAndSeen(@Param("userId") Long userId, @Param("seen") boolean seen);
}
