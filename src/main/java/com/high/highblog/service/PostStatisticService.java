package com.high.highblog.service;

import com.high.highblog.enums.VoteType;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.PostStatistic;
import com.high.highblog.repository.PostStatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class PostStatisticService {

    private final PostStatisticRepository repository;

    public PostStatisticService(final PostStatisticRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PostStatistic> fetchByPostIdIn(Collection<Long> postIds) {
        log.info("Fetch list post statistics by post id in #{}", postIds);

        return repository.findByPostIdIn(postIds);
    }

    @Transactional
    public void saveNew(PostStatistic postStatistic) {
        log.info("Save new post statistic with data #{}", postStatistic);
        validatePostBeforeSaveNew(postStatistic);

        repository.save(postStatistic);
    }

    @Transactional
    public void saveNumberOfVoteBaseOnPostIdAndVoteType(final Long postId, final VoteType voteType) {
        log.info("Save number of vote by voteType #{}", voteType);

        PostStatistic postStatistic = repository.findByPostId(postId)
                                                .orElseThrow(() -> new ObjectNotFoundException("postStatistic"));

        switch (voteType) {
            case UP:
                postStatistic.setNumberOfVotes(postStatistic.getNumberOfVotes() + 1);
                break;
            case DOWN:
                postStatistic.setNumberOfVotes(postStatistic.getNumberOfVotes() - 1);
        }
    }

    private void validatePostBeforeSaveNew(final PostStatistic postStatistic) {
        if (ObjectUtils.isNotEmpty(postStatistic.getId()))
            throw new ValidatorException("Invalid post statistic id", "id");
    }
}