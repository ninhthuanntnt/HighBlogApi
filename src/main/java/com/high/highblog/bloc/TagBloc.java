package com.high.highblog.bloc;

import com.high.highblog.model.entity.Tag;
import com.high.highblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TagBloc {

    private final TagService tagService;

    public TagBloc(final TagService tagService) {
        this.tagService = tagService;
    }

    public List<Tag> fetchAllTags() {
        log.info("Fetch all tags");

        // TODO: fetch tag with pagination and search for auto-complete
        return tagService.fetchAllTags();
    }
}
