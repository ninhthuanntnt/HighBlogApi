package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.PostListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.admin.AdminPostSearchReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import com.high.highblog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminPostListController")
@RequestMapping("/api/v1/admin/posts")
public class PostListController {
    private final PostListBloc postListBloc;

    public PostListController(final PostListBloc postListBloc) {
        this.postListBloc = postListBloc;
    }

    // TODO: bind array of String to array of Objects fot sorts field
    @GetMapping
    public ResponseEntity<BasePaginationRes> searchDynamicPosts(final Long categoryId, AdminPostSearchReq req) {
        Page<Post> posts = postListBloc.searchDynamicPosts(categoryId,req);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(
                posts.map(PostMapper.INSTANCE::toAdminPostRes)
        ));
    }
}