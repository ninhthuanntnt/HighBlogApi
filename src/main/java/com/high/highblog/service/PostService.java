package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.Post;
import com.high.highblog.repository.PostRepository;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostService {
    private final PostRepository repository;

    public PostService(final PostRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveNew(final Post post) {
        log.info("Save new post with data #{}", post);
        validatePostBeforeSaveNew(post);

        repository.save(post);
    }

    @Transactional
    public void save(final Post post){
        log.info("Save post with id #{}", post.getId());
        validatePostBeforeSave(post);

        repository.save(post);
    }

    @Transactional(readOnly = true)
    public Post getById(final Long id) {
        log.info("Get post by id #{}", id);

        return repository.findById(id)
                         .orElseThrow(() -> new ObjectNotFoundException("post"));
    }

    private void validatePostBeforeSaveNew(final Post post) {
        if (ObjectUtils.isNotEmpty(post.getId()))
            throw new ValidatorException("Invalid post id", "id");
    }
    private void validatePostBeforeSave(final Post post) {
        if (ObjectUtils.isEmpty(post.getId()))
            throw new ValidatorException("Invalid post id", "id");
    }
}
