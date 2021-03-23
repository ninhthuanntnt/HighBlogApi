package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.repository.PostTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostTagService {
    private final PostTagRepository repository;

    public PostTagService(final PostTagRepository repository) {
        this.repository = repository;
    }

    public void saveNew(final PostTag postTag) {
        log.info("Save new post tag with postId #{} and tagId #{}", postTag.getPostId(), postTag.getTagId());
        validatePostBeforeSaveNew(postTag);

        repository.save(postTag);
    }

    public void saveNew(final List<PostTag> postTag) {
        log.info("Save new post tag with data #{}", postTag);

        validatePostBeforeSaveNew(postTag);

        repository.saveAll(postTag);
    }

    private void validatePostBeforeSaveNew(final PostTag postTag) {
        if (ObjectUtils.isNotEmpty(postTag.getId()))
            throw new ValidatorException("Invalid post tag id", "id");
    }

    private void validatePostBeforeSaveNew(final List<PostTag> postTags) {
        if (ObjectUtils.isNotEmpty(postTags))
            postTags.forEach(postTag -> {
                if (ObjectUtils.isEmpty(postTag.getId())) {
                    throw new ValidatorException("Invalid post tag id", "id");
                }
            });
    }
}
