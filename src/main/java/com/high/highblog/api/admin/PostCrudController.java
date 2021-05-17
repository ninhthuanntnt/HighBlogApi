package com.high.highblog.api.admin;


import com.high.highblog.bloc.admin.PostCrudBloc;
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
    @DeleteMapping("/soft-delete")
    public ResponseEntity<?> softDeletePost(@RequestParam final Long id, final String nickName) {

        postCrudBloc.softDeletePost(id,nickName);

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/restore-post")
    public ResponseEntity<?> restorePost(@RequestParam final Long id, final  String nickName){
        postCrudBloc.restorePost(id,nickName);
        return ResponseEntity.noContent().build();
    }
}
