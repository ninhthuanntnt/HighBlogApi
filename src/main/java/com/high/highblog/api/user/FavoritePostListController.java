package com.high.highblog.api.user;

import com.high.highblog.bloc.FavoritePostListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import com.high.highblog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/favorite-posts")
public class FavoritePostListController {

    private final FavoritePostListBloc favoritePostListBloc;

    public FavoritePostListController(final FavoritePostListBloc favoritePostListBloc) {
        this.favoritePostListBloc = favoritePostListBloc;
    }

    @GetMapping
    public ResponseEntity<BasePaginationRes> fetchListFavoritePost(final BasePaginationReq basePaginationReq) {

        Page<Post> posts = favoritePostListBloc.fetchFavoritePostForCurrentUser(basePaginationReq);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(posts.map(PostMapper.INSTANCE::toPostRes)));
    }
}
