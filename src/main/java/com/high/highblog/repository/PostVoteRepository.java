package com.high.highblog.repository;

import com.high.highblog.model.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

    Optional<PostVote> findByIdAndUserId(Long id, Long userId);
    Optional<PostVote> findByPostIdAndUserId(Long postId, Long userId);
}
