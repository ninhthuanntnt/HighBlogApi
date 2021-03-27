package com.high.highblog.repository;

import com.high.highblog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository
        extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT p.* FROM hb_posts AS p"
                + " WHERE MATCH (p.title, p.summary, p.content) AGAINST (:keyword IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    Page<Post> searchFullTextPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Post p"
                + " WHERE p.title LIKE %:keyword%"
                + " OR p.summary LIKE %:keyword%"
                + " OR p.content LIKE %:keyword%")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);
}
