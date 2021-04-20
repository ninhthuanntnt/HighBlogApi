package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.CommentMapper;
import com.high.highblog.model.dto.request.CommentCreateReq;
import com.high.highblog.model.dto.request.CommentListReq;
import com.high.highblog.model.dto.request.CommentUpdateReq;
import com.high.highblog.model.entity.Comment;
import com.high.highblog.model.entity.Post;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.CommentService;
import com.high.highblog.service.PostStatisticService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommentCrudBloc {

    private final CommentService commentService;
    private final PostStatisticService postStatisticService;

    public CommentCrudBloc(final CommentService commentService,
                           final PostStatisticService postStatisticService) {
        this.commentService = commentService;
        this.postStatisticService = postStatisticService;
    }

    @Transactional
    public Long createCommentForCurrentUser(final CommentCreateReq commentCreateReq) {
        log.info("Create comment with data #{}", commentCreateReq);

        Comment comment = CommentMapper.INSTANCE.toComment(commentCreateReq);

        comment.setUserId(SecurityHelper.getUserId());

        commentService.saveNew(comment);
        postStatisticService.increaseNumberOfComments(commentCreateReq.getPostId());

        return comment.getId();
    }


    @Transactional
    public void updateCommentForCurrentUser(final Long id, final CommentUpdateReq commentUpdateReq) {
        Long userId = SecurityHelper.getUserId();
        log.info("Update comment by comment id #{} for current user with user id #{}", id, userId);

        Comment comment = commentService.getByIdAndUserId(id, userId);

        comment.setContent(commentUpdateReq.getContent());

        commentService.save(comment);
    }

    @Transactional
    public void deleteCommentForCurrentUser(final Long id) {
        Long userId = SecurityHelper.getUserId();
        log.info("Delete comment by id #{} with userId #{}", id, userId);

        Long postId = commentService.getByIdAndUserId(id, userId).getPostId();

        commentService.delete(id, userId);
        postStatisticService.decreaseNumberOfComments(postId);
    }
}
