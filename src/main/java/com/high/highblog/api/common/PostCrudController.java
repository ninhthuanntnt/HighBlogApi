package com.high.highblog.api.common;

import com.high.highblog.bloc.PostCrudBloc;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.response.PostDetailRes;
import com.high.highblog.model.entity.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostCrudController {
    private final PostCrudBloc postCrudBloc;

    public PostCrudController(final PostCrudBloc postCrudBloc) {
        this.postCrudBloc = postCrudBloc;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailRes> getPostDetail(@PathVariable Long id) {

        Post post = postCrudBloc.getPostDetail(id);

        return ResponseEntity.ok(PostMapper.INSTANCE.toPostDetailRes(post));
    }
}
