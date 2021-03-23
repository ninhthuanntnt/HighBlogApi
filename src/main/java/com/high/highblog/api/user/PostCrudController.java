package com.high.highblog.api.user;

import com.high.highblog.bloc.PostCrudBloc;
import com.high.highblog.model.dto.request.PostCreateReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
