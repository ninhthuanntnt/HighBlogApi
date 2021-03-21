package com.high.highblog.service;

import com.high.highblog.model.entity.Tag;
import com.high.highblog.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
