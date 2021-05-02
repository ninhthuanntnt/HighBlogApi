package com.high.highblog.repository;

import com.high.highblog.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {
    @Query("SELECT CASE WHEN count(sub) > 0 THEN TRUE ELSE FALSE END "
            + " FROM Subscription sub"
            + " WHERE sub.userId = :userId AND sub.followerId = :followerId")
    boolean existsByUserIdAndFollowerId(@Param("userId") Long userId, @Param("followerId") Long followerId);

    Optional<Subscription> findByUserIdAndFollowerId(Long userId, Long followerId);

    List<Subscription> findDistinctByFollowerId(Long followerId);

    @Query("SELECT s.followerId FROM Subscription s"
            + " WHERE s.userId = :userId")
    List<Long> findFollowerIdsByUserId(@Param("userId") Long userId);
}
