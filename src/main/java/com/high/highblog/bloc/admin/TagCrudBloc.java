package com.high.highblog.bloc.admin;

import com.high.highblog.model.dto.request.admin.AdminTagReq;

import com.high.highblog.model.entity.Tag;
import com.high.highblog.service.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Component("adminTagCrudBloc")
public class TagCrudBloc {
    private final TagService tagService;

    public TagCrudBloc(TagService tagService) {
        this.tagService = tagService;
    }

    @Transactional
    public void addNewTag(final AdminTagReq req) {
        log.info("add #{} to tags",req.getTagName());
        Tag newTag = new Tag();
        newTag.setName(req.getTagName());
        tagService.addNewTag(newTag);
    }
}
