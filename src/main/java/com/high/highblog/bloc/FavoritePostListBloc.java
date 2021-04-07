package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Post;
import com.high.highblog.service.FavoritePostService;
import com.high.highblog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class FavoritePostListBloc {

    private final PostService postService;

    public FavoritePostListBloc(final PostService postService) {
        this.postService = postService;
    }

    @Transactional
    public Page<Post> fetchFavoritePostForCurrentUser(final BasePaginationReq basePaginationReq){
        log.info("Fetch favorite post for current user");

        PageRequest pageRequest = PaginationHelper.generatePageRequest(basePaginationReq);

        return postService.fetchFavoritePostByUserIdWithPageRequest(SecurityHelper.getUserId(), pageRequest);
    }
}
