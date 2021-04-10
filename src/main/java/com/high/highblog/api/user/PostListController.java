package com.high.highblog.api.user;

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
import org.springframework.web.bind.annotation.RestController;

@RestController("userPostListController")
@RequestMapping("/api/v1/user/posts")
public class PostListController {

    private final PostListBloc postListBloc;

    public PostListController(final PostListBloc postListBloc) {
        this.postListBloc = postListBloc;
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<BasePaginationRes> fetchListSubscriptionPosts(BasePaginationReq basePaginationReq) {
        Page<Post> posts = postListBloc.fetchSubscriptionPostsForCurrentUser(basePaginationReq);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(posts.map(PostMapper.INSTANCE::toPostRes)));
    }
}
