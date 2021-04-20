package com.high.highblog.bloc;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.FavoritePostCreateReq;
import com.high.highblog.model.entity.PostStatistic;
import com.high.highblog.service.FavoritePostService;
import com.high.highblog.service.PostStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class FavoritePostCrudBloc {
    private final FavoritePostService favoritePostService;
    private final PostStatisticService postStatisticService;

    public FavoritePostCrudBloc(final FavoritePostService favoritePostService,
                                final PostStatisticService postStatisticService) {
        this.favoritePostService = favoritePostService;
        this.postStatisticService = postStatisticService;
    }

    @Transactional
    public void createFavoritePostForCurrentUser(final FavoritePostCreateReq req) {
        log.info("Create favorite post for current user with postId #{}", req.getPostId());
        validateFavoritePostCreateReq(req);
        favoritePostService.saveNew(req.getPostId(), SecurityHelper.getUserId());
        postStatisticService.increaseNumberOfFavorite(req.getPostId());
    }

    @Transactional
    public void deleteFavoritePostForCurrentUser(final Long postId) {
        log.info("Delete favorite post for current user with postId #{}", postId);

        favoritePostService.delete(postId, SecurityHelper.getUserId());
        postStatisticService.decreaseNumberOfFavorite(postId);
    }

    private void validateFavoritePostCreateReq(final FavoritePostCreateReq req) {
        if (favoritePostService.existsByPostIdAndUserId(req.getPostId(), SecurityHelper.getUserId())){
            throw new ValidatorException("Already added to favorite", "favoritePost");
        }
    }
}
