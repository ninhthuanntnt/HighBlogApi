package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.FavoritePost;
import com.high.highblog.repository.FavoritePostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class FavoritePostService {
    private final FavoritePostRepository repository;

    public FavoritePostService(final FavoritePostRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveNew(final FavoritePost favoritePost) {
        log.info("Save new favorite post with data #{}", favoritePost);
        validateFavoritePostBeforeSaveNew(favoritePost);
        repository.save(favoritePost);
    }

    @Transactional
    public void saveNew(final Long postId, final Long userId) {
        log.info("Save new favorite post with postId #{} and userId #{}", postId, userId);

        saveNew(FavoritePost.builder()
                            .postId(postId)
                            .userId(userId)
                            .build());
    }

    private void validateFavoritePostBeforeSaveNew(final FavoritePost favoritePost) {
        if (ObjectUtils.isEmpty(favoritePost) || ObjectUtils.isNotEmpty(favoritePost.getId()))
            throw new ValidatorException("Invalid favorite post id", "id");
        if (favoritePost.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }
}
