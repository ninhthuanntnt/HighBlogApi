package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostTag;
import com.high.highblog.repository.PostTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PostTagService {
    private final PostTagRepository repository;

    public PostTagService(final PostTagRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveNew(final PostTag postTag) {
        log.info("Save new post tag with postId #{} and tagId #{}", postTag.getPostId(), postTag.getTagId());

        validatePostBeforeSaveNew(postTag);
        repository.save(postTag);
    }

    @Transactional
    public void saveNew(final List<PostTag> postTag) {
        log.info("Save new post tag with data #{}", postTag);

        validatePostBeforeSaveNew(postTag);
        repository.saveAll(postTag);
    }

    @Transactional(readOnly = true)
    public List<PostTag> getByPostId(final Long postId) {
        log.info("Get post tag by postId #{}", postId);

        return repository.getByPostId(postId);
    }

    @Transactional
    public void deleteOldAndSaveNew(final Long postId, final List<PostTag> postTags) {
        log.info("Delete old post tag with postId #{} and save new with data #{}", postId, postTags);
        List<PostTag> oldPostTags = repository.findByPostId(postId);
        repository.deleteAll(oldPostTags);

        validatePostBeforeSaveNew(postTags);
        repository.saveAll(postTags);
    }

    @Transactional(readOnly = true)
    public List<PostTag> fetchPostTagsByPostIdIn(List<Long> postIds) {
        log.info("Fetch post tags by postId in #{}", postIds);

        if(ObjectUtils.isEmpty(postIds))
            return Collections.emptyList();

        return repository.findByPostIdIn(postIds);
    }

    private void validatePostBeforeSaveNew(final PostTag postTag) {
        if (ObjectUtils.isNotEmpty(postTag.getId()))
            throw new ValidatorException("Invalid post tag id", "id");
    }

    private void validatePostBeforeSaveNew(final List<PostTag> postTags) {
        if (ObjectUtils.isNotEmpty(postTags))
            postTags.forEach(postTag -> {
                if (ObjectUtils.isNotEmpty(postTag.getId())) {
                    throw new ValidatorException("Invalid post tag id", "id");
                }
            });
    }
}
