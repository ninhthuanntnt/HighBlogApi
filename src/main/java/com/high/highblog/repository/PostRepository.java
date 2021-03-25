package com.high.highblog.repository;

import com.high.highblog.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository
        extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long id, Long userId);
}
