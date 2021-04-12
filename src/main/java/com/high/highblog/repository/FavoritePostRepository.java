package com.high.highblog.repository;

import com.high.highblog.model.entity.FavoritePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoritePostRepository extends JpaRepository<FavoritePost, Long> {

    Optional<FavoritePost> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
