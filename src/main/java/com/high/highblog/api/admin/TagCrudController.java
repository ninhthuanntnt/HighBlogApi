package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.TagCrudBloc;
import com.high.highblog.model.dto.request.admin.AdminTagReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
