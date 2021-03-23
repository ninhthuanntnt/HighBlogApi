package com.high.highblog.repository;

import com.high.highblog.model.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository
        extends JpaRepository<PostTag, Long> {
}
