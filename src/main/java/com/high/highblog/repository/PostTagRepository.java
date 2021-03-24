package com.high.highblog.repository;

import com.high.highblog.model.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository
        extends JpaRepository<PostTag, Long> {

    @Query("SELECT new PostTag(pt.tagId, t.name) FROM PostTag pt"
            + " JOIN Tag t ON t.id = pt.tagId"
            + " WHERE pt.postId = :postId")
    List<PostTag> getByPostId(@Param("postId") Long postId);
}
