package com.high.highblog.api.admin;


import com.high.highblog.bloc.admin.PostCrudBloc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminPostCrudController")
@RequestMapping("/api/v1/admin/posts")
public class PostCrudController {

    private final PostCrudBloc postCrudBloc;

    public PostCrudController(final PostCrudBloc postCrudBloc) {
        this.postCrudBloc = postCrudBloc;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable final Long id) {

        postCrudBloc.deletePost(id);

        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeletePost(@PathVariable final Long id) {

        postCrudBloc.softDeletePost(id);

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/restore-post/{id}")
    public ResponseEntity<?> restorePost(@PathVariable final Long id){
        postCrudBloc.restorePost(id);
        return ResponseEntity.noContent().build();
    }
}
