package com.high.highblog.api.user;

import com.high.highblog.bloc.PostCrudBloc;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.dto.request.PostUpdateReq;
import com.high.highblog.model.dto.response.PostDetailToUpdateRes;
import com.high.highblog.model.entity.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userPostCrudController")
@RequestMapping("/api/v1/user/posts")
public class PostCrudController {

    private final PostCrudBloc postCrudBloc;

    public PostCrudController(final PostCrudBloc postCrudBloc) {
        this.postCrudBloc = postCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody final PostCreateReq postCreateReq) {

        postCrudBloc.createPost(postCreateReq);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailToUpdateRes> getPostDetailForCurrentUser(@PathVariable Long id) {

        Post post = postCrudBloc.getPostDetailForCurrentUser(id);

        return ResponseEntity.ok(PostMapper.INSTANCE.toPostDetailToUpdateRes(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable final Long id,
                                        @RequestBody final PostUpdateReq postUpdateReq) {
        postCrudBloc.updatePostForCurrentUser(id, postUpdateReq);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable final Long id) {

        postCrudBloc.deletePostForCurrentUser(id);

        return ResponseEntity.noContent().build();
    }
}
