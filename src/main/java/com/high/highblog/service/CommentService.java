package com.high.highblog.service;

import com.high.highblog.model.entity.Comment;
import com.high.highblog.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository repository;

    public CommentService(final CommentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Comment> fetchByPostId(final Long postId) {
        log.info("Fetch comments by post id #{}", postId);

        return repository.findByPostId(postId);
    }
}
