package com.high.highblog.repository;

import com.high.highblog.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {
    @Query("SELECT CASE WHEN count(sub) > 0 THEN TRUE ELSE FALSE END "
            + " FROM Subscription sub"
            + " JOIN User u ON u.id = sub.followerId"
            + " WHERE sub.userId = :userId AND u.nickName = :nickName")
    boolean existsByUserIdAndNickName(@Param("userId") Long userId, @Param("nickName") String nickName);

}
