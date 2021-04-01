package com.high.highblog.bloc;

import com.high.highblog.constant.AppErrorCode;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.PostVoteMapper;
import com.high.highblog.model.dto.request.PostVoteCreateReq;
import com.high.highblog.model.dto.request.PostVoteDeleteReq;
import com.high.highblog.model.dto.request.PostVoteUpdateReq;
import com.high.highblog.model.entity.PostVote;
import com.high.highblog.service.PostStatisticService;
import com.high.highblog.service.PostVoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class PostVoteCrudBloc {
    private final PostVoteService postVoteService;
    private final PostStatisticService postStatisticService;

    public PostVoteCrudBloc(final PostVoteService postVoteService,
                            final PostStatisticService postStatisticService) {
        this.postVoteService = postVoteService;
        this.postStatisticService = postStatisticService;
    }

    @Transactional
    public void createPostVoteForCurrentUser(final PostVoteCreateReq postVoteCreateReq) {
        Long userId = SecurityHelper.getUserId();
        log.info("Create post vote with data #{} for current user #{}", postVoteCreateReq, userId);

        validatePostVoteCreateReq(postVoteCreateReq);

        PostVote postVote = PostVoteMapper.INSTANCE.toPostVote(postVoteCreateReq);

        postVote.setUserId(userId);
        postVoteService.saveNew(postVote);

        postStatisticService.saveNumberOfVoteAfterVote(postVoteCreateReq.getPostId(),
                                                       postVoteCreateReq.getVoteType());
    }

    @Transactional
    public void updatePostVoteForCurrentUser(final PostVoteUpdateReq postVoteUpdateReq) {
        Long userId = SecurityHelper.getUserId();
        log.info("Create post vote with data #{} for current user #{}", postVoteUpdateReq, userId);

        PostVote currentPostVote = postVoteService.getByPostIdAndUserId(postVoteUpdateReq.getPostId(), userId);

        validateCurrentPostVoteAndPostVoteUpdateReq(currentPostVote, postVoteUpdateReq);

        PostVote newPostVote = PostVoteMapper.INSTANCE.toPostVote(postVoteUpdateReq, currentPostVote);

        postVoteService.save(newPostVote);

        postStatisticService.saveNumberOfVoteAfterVote(postVoteUpdateReq.getPostId(),
                                                       postVoteUpdateReq.getVoteType());
    }

    @Transactional
    public void deletePostVoteForCurrentUser(final PostVoteDeleteReq postVoteDeleteReq) {
        Long userId = SecurityHelper.getUserId();
        log.info("Delete post vote with data #{} for current user #{}", postVoteDeleteReq, userId);

        PostVote currentPostVote = postVoteService.getByPostIdAndUserId(postVoteDeleteReq.getPostId(), userId);

        validateCurrentPostVoteAndPostVoteDeleteReq(currentPostVote, postVoteDeleteReq);

        postVoteService.delete(currentPostVote);

        postStatisticService.saveNumberOfVoteAfterDelete(postVoteDeleteReq.getPostId(),
                                                         postVoteDeleteReq.getPreviousVoteType());

    }

    private void validatePostVoteCreateReq(final PostVoteCreateReq postVoteCreateReq) {
        PostVote postVote = postVoteService.getByPostIdAndUserId(postVoteCreateReq.getPostId(),
                                                                 SecurityHelper.getUserId());

        if (ObjectUtils.isNotEmpty(postVote))
            throw new ValidatorException("Already vote", AppErrorCode.DEFAULT_VALIDATOR);
    }

    private void validateCurrentPostVoteAndPostVoteUpdateReq(final PostVote currentPostVote,
                                                             final PostVoteUpdateReq postVoteUpdateReq) {

        if (ObjectUtils.isEmpty(currentPostVote))
            throw new ValidatorException("Not created vote", AppErrorCode.DEFAULT_VALIDATOR);
        else if (currentPostVote.getVoteType().equals(postVoteUpdateReq.getVoteType()))
            throw new ValidatorException("Already vote", AppErrorCode.DEFAULT_VALIDATOR);
    }

    private void validateCurrentPostVoteAndPostVoteDeleteReq(final PostVote currentPostVote,
                                                             final PostVoteDeleteReq postVoteDeleteReq) {

        if (ObjectUtils.isEmpty(currentPostVote))
            throw new ObjectNotFoundException("Not found postVote");
        else if (!currentPostVote.getVoteType().equals(postVoteDeleteReq.getPreviousVoteType()))
            throw new ValidatorException("Not is the same vote type", AppErrorCode.DEFAULT_VALIDATOR);
    }
}
