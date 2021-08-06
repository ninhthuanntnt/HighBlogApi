package com.high.highblog.bloc.admin;

import com.high.highblog.model.dto.request.admin.AdminDeleteTagReq;
import com.high.highblog.model.dto.request.admin.AdminTagReq;
import com.high.highblog.model.dto.request.admin.AdminUpdateTagReq;
import com.high.highblog.model.entity.Tag;
import com.high.highblog.service.PostTagService;
import com.high.highblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component("adminTagCrudBloc")
public class TagCrudBloc {
    private final TagService tagService;
    private final PostTagService postTagService;

    public TagCrudBloc(TagService tagService, PostTagService postTagService) {
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

    @Transactional
    public void addNewTag(final AdminTagReq req) {
        log.info("add #{} to tags", req.getTagName());
        Tag newTag = new Tag();
        newTag.setName(req.getTagName());
        tagService.addNewTag(newTag);
    }

    @Transactional
    public void updateTag(final AdminUpdateTagReq req) {
        Long tagId = req.getId();
        String tagName = req.getTagName();
        log.info("update tag #{}", tagName);
        tagService.updateTag(tagId, tagName);
    }

    @Transactional
    public void deleteTag(final AdminDeleteTagReq req) {
        Long tagId = req.getId();
        log.info("delete tag with tagId#{} ",tagId);
        postTagService.deleteByTagId(tagId);
        tagService.deleteTag(tagId);

    }
}
