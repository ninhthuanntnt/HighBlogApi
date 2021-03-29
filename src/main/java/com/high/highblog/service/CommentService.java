package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.Comment;
import com.high.highblog.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
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
    public List<Comment> fetchByPostIdWithPageRequest(final Long postId, final PageRequest pageRequest) {
        log.info("Fetch comments by post id #{} with pageRequest #{}", postId, pageRequest);

        return repository.findByPostId(postId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Comment getByIdAndUserId(final Long id, final Long userId) {
        log.info("Get comment by id #{} and userId #{}", id, userId);

        return repository.findByIdAndUserId(id, userId)
                         .orElseThrow(() -> new ValidatorException("Invalid comment to update", "comment"));
    }

    @Transactional
    public void saveNew(final Comment comment) {
        log.info("Save new comment with data #{}", comment);
        validateCommentBeforeSaveNew(comment);

        repository.save(comment);
    }

    @Transactional
    public void save(final Comment comment) {
        log.info("Save comment with data #{}", comment);
        validateCommentBeforeSave(comment);

        repository.save(comment);
    }


    private void validateCommentBeforeSaveNew(final Comment comment) {
        if (ObjectUtils.isNotEmpty(comment.getId()))
            throw new ValidatorException("Invalid comment id", "id");
        if (ObjectUtils.isEmpty(comment.getUserId()) && comment.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }
    private void validateCommentBeforeSave(final Comment comment) {
        if (ObjectUtils.isEmpty(comment.getId()))
            throw new ValidatorException("Invalid comment id", "id");
        if (ObjectUtils.isEmpty(comment.getUserId()) && comment.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }

}
