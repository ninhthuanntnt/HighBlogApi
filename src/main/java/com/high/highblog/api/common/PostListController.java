package com.high.highblog.api.common;

import com.high.highblog.bloc.PostListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.request.PostSearchReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import com.high.highblog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostListController {
    private final PostListBloc postListBloc;

    public PostListController(final PostListBloc postListBloc) {
        this.postListBloc = postListBloc;
    }

    // TODO: bind array of String to array of Objects fot sorts field
    @GetMapping
    public ResponseEntity<BasePaginationRes> fetchListPost(final BasePaginationReq req) {
        Page<Post> posts = postListBloc.fetchPosts(req);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(
                posts.map(PostMapper.INSTANCE::toPostRes)
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(final PostSearchReq req) {
        Page<Post> posts = postListBloc.searchPosts(req);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(
                posts.map(PostMapper.INSTANCE::toPostRes)
        ));
    }

}
