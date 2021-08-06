package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.TagCrudBloc;
import com.high.highblog.model.dto.request.admin.AdminTagReq;
import com.high.highblog.model.dto.request.admin.AdminUpdateTagReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminTagCrudController")
@RequestMapping("/api/v1/admin/tags")
public class TagCrudController {
    private final TagCrudBloc tagCrudBloc;

    public TagCrudController(final TagCrudBloc tagCrudBloc) {
        this.tagCrudBloc = tagCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> addNewTag(@RequestBody final AdminTagReq adminTagReq) {
        tagCrudBloc.addNewTag(adminTagReq);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<?> updateTag(@RequestBody final AdminUpdateTagReq adminUpdateTagReq){
        tagCrudBloc.updateTag(adminUpdateTagReq);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable final Long id){
        tagCrudBloc.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
