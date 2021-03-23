package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.Post;
import com.high.highblog.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostService {
    private final PostRepository repository;

    public PostService(final PostRepository repository) {
        this.repository = repository;
    }

    public void saveNew(final Post post){
        log.info("Save new post with id");
        validatePostBeforeSaveNew(post);

        repository.save(post);
    }

    private void validatePostBeforeSaveNew(final Post post){
        if(ObjectUtils.isNotEmpty(post.getId()))
            throw new ValidatorException("Invalid post id", "id");
    }
}
