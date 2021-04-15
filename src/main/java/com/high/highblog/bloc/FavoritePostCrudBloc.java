package com.high.highblog.bloc;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.FavoritePostCreateReq;
import com.high.highblog.service.FavoritePostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class FavoritePostCrudBloc {
    private final FavoritePostService favoritePostService;

    public FavoritePostCrudBloc(final FavoritePostService favoritePostService) {
        this.favoritePostService = favoritePostService;
    }

    @Transactional
    public void createFavoritePostForCurrentUser(final FavoritePostCreateReq req) {
        log.info("Create favorite post for current user with postId #{}", req.getPostId());
        validateFavoritePostCreateReq(req);
        favoritePostService.saveNew(req.getPostId(), SecurityHelper.getUserId());
    }

    @Transactional
    public void deleteFavoritePostForCurrentUser(final Long postId) {
        log.info("Delete favorite post for current user with postId #{}", postId);

        favoritePostService.delete(postId, SecurityHelper.getUserId());
    }

    private void validateFavoritePostCreateReq(final FavoritePostCreateReq req) {
        if (favoritePostService.existsByPostIdAndUserId(req.getPostId(), SecurityHelper.getUserId())){
            throw new ValidatorException("Already added to favorite", "favoritePost");
        }
    }
}
