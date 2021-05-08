package com.high.highblog.api.admin;

import com.high.highblog.bloc.PostCrudBloc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminPostCrudController")
@RequestMapping("/api/v1/admin/post")
public class PostCrudController {

    private final PostCrudBloc postCrudBloc;

    public PostCrudController(final PostCrudBloc postCrudBloc) {
        this.postCrudBloc = postCrudBloc;
    }

    @DeleteMapping()
    public ResponseEntity<?> deletePost(@RequestParam final Long id, final String nickName) {

        postCrudBloc.deletePost(id,nickName);

        return ResponseEntity.noContent().build();
    }
}
