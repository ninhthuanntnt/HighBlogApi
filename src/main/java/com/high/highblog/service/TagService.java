package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.Tag;
import com.high.highblog.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TagService {

    private final TagRepository repository;

    public TagService(final TagRepository repository) {
        this.repository = repository;
    }

    public List<Tag> fetchAllTags(){
        log.info("Fetch all tags");

        return repository.findAll();
    }
    @Transactional
    public void addNewTag(final Tag newTag) {
        validatePostBeforeSave(newTag.getName());
        log.info("add new tag");
        repository.save(newTag);
    }
    private void validatePostBeforeSave(final String tagName){
        if (repository.existsTagByNameEquals(tagName))
            throw new ValidatorException("TagName already exists", "tagName");
    }
}
