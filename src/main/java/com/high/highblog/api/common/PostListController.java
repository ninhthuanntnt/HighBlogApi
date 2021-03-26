package com.high.highblog.api.common;

import com.high.highblog.bloc.PostListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import com.high.highblog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostListController {
    private final PostListBloc postListBloc;

    public PostListController(final PostListBloc postListBloc) {
        this.postListBloc = postListBloc;
    }

    @GetMapping
    public ResponseEntity<BasePaginationRes> fetchListPost(@RequestParam Integer page,
                                                           @RequestParam("page_size") Integer pageSize,
                                                           @RequestParam(name = "sorts", required = false)
                                                                       String[] sorts) {
        BasePaginationReq req = new BasePaginationReq(page, pageSize, sorts);
        Page<Post> posts = postListBloc.fetchPosts(req);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(
                posts.map(PostMapper.INSTANCE::toPostRes)
        ));
    }
}
