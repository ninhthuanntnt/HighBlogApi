package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.Post;
import com.high.highblog.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public void save(final Post post) {
        log.info("Save post with id #{}", post.getId());
        validatePostBeforeSave(post);

        repository.save(post);
    }

    @Transactional(readOnly = true)
    public Post getByIdAndUserId(final Long id, final Long userId) {
        log.info("Get post by id #{} and userId #{}", id, userId);

        return repository.findByIdAndUserId(id, userId)
                         .orElseThrow(() -> new ObjectNotFoundException("post"));
    }

    @Transactional(readOnly = true)
    public Post getById(final Long id) {
        log.info("Get post by id #{}", id);

        return repository.findById(id)
                         .orElseThrow(() -> new ObjectNotFoundException("post"));
    }

    @Transactional(readOnly = true)
    public Page<Post> fetchPostsWithPageRequest(final PageRequest pageRequest) {
        log.info("Fetch post by page request");

        return repository.findAll(pageRequest);
    }

    @Transactional
    public Page<Post> searchPostsByKeywordWithPageRequest(final String keyword,
                                                          final PageRequest pageRequest) {
        log.info("Search post with keyword #{}", keyword);

        if (keyword.length() <= 2)
            return repository.searchPosts(keyword, pageRequest);
        else
            return repository.searchFullTextPosts(keyword, pageRequest);
    }

    private void validatePostBeforeSaveNew(final Post post) {
        if (ObjectUtils.isNotEmpty(post.getId()))
            throw new ValidatorException("Invalid post id", "id");
        if (post.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }

    private void validatePostBeforeSave(final Post post) {
        if (ObjectUtils.isEmpty(post.getId()))
            throw new ValidatorException("Invalid post id", "id");
        if (post.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }
}
