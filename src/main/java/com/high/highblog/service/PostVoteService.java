package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.PostVote;
import com.high.highblog.repository.PostVoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostVoteService {

    private final PostVoteRepository repository;

    public PostVoteService(final PostVoteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PostVote getNullableByPostIdAndUserId(final Long id, final Long userId) {
        log.info("Get nullable post vote by id #{} and user id #{}", id, userId);

        return repository.findByPostIdAndUserId(id, userId)
                         .orElse(null);
    }

    @Transactional(readOnly = true)
    public PostVote getByPostIdAndUserId(final Long id, final Long userId) {
        log.info("Get post vote by id #{} and user id #{}", id, userId);

        return repository.findByPostIdAndUserId(id, userId)
                         .orElseThrow(()-> new ObjectNotFoundException("postVote"));
    }

    @Transactional
    public void saveNew(final PostVote postVote) {
        log.info("Save new post vote with data #{}", postVote);
        validatePostVoteBeforeSaveNew(postVote);

        repository.save(postVote);
    }

    @Transactional
    public void save(final PostVote postVote) {
        log.info("Save post vote with data #{}", postVote);
        validatePostVoteBeforeSave(postVote);

        repository.save(postVote);
    }

    @Transactional
    public void delete(final PostVote postVote){
        log.info("Delete post vote with data #{}", postVote);

        repository.delete(postVote);
    }

    private void validatePostVoteBeforeSaveNew(final PostVote postVote) {
        if (ObjectUtils.isNotEmpty(postVote.getId()))
            throw new ValidatorException("Invalid post id", "id");
        if (postVote.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }

    private void validatePostVoteBeforeSave(final PostVote postVote) {
        if (ObjectUtils.isEmpty(postVote.getId()))
            throw new ValidatorException("Invalid post id", "id");
        if (postVote.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }
}
