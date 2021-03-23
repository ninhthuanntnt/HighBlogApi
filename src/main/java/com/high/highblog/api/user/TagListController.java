package com.high.highblog.api.user;

import com.high.highblog.bloc.TagListBloc;
import com.high.highblog.model.dto.response.TagRes;
import com.high.highblog.model.entity.Tag;
import com.high.highblog.mapper.TagMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/tags")
public class TagListController {

    private final TagListBloc tagListBloc;

    public TagListController(final TagListBloc tagListBloc) {
        this.tagListBloc = tagListBloc;
    }

    @GetMapping
    public ResponseEntity<List<TagRes>> fetchTags() {
        List<Tag> tags = tagListBloc.fetchAllTags();
        return ResponseEntity.ok(TagMapper.INSTANCE.toListTagRes(tags));
    }
}
