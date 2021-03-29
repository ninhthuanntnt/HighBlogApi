package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.CommentMapper;
import com.high.highblog.model.dto.request.CommentCreateReq;
import com.high.highblog.model.dto.request.CommentListReq;
import com.high.highblog.model.dto.request.CommentUpdateReq;
import com.high.highblog.model.entity.Comment;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.CommentService;
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
public class CommentBloc {

    private final CommentService commentService;
    private final UserService userService;

    public CommentBloc(final CommentService commentService,
                       final UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @Transactional
    public Long createCommentForCurrentUser(final CommentCreateReq commentCreateReq) {
        log.info("Create comment with data #{}", commentCreateReq);

        Comment comment = CommentMapper.INSTANCE.toComment(commentCreateReq);

        comment.setUserId(SecurityHelper.getUserId());

        commentService.saveNew(comment);

        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<Comment> fetchCommentsByPostId(final CommentListReq commentListReq) {
        log.info("Fetch comments with data #{}", commentListReq);
        PageRequest pageRequest = PaginationHelper.generatePageRequest(commentListReq);
        List<Comment> comments = commentService.fetchByPostIdWithPageRequest(commentListReq.getPostId(),
                                                                             pageRequest);

        includeUserToComments(comments);

        return populateComments(comments);
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

        commentService.delete(id, userId);
    }

    private void includeUserToComments(List<Comment> comments) {
        Map<Long, User> users = userService.fetchByIdIn(comments.stream()
                                                                .map(Comment::getUserId)
                                                                .collect(Collectors.toList()))
                                           .stream()
                                           .collect(Collectors.toMap(User::getId, user -> user));

        comments.forEach(comment -> comment.setUser(users.get(comment.getUserId())));
    }

    private List<Comment> populateComments(final List<Comment> comments) {
        List<Comment> rootComment = comments.stream()
                                            .filter(comment -> ObjectUtils.isEmpty(comment.getParentId()))
                                            .collect(Collectors.toList());

        Map<Long, List<Comment>> childCommentsMap =
                comments.stream()
                        .filter(comment -> ObjectUtils.isNotEmpty(comment.getParentId()))
                        .sorted((o1, o2) -> Long.compare(o2.getId(), o1.getId()))
                        .collect(Collectors.groupingBy(Comment::getParentId));


        rootComment.forEach(comment -> comment.setChildComments(childCommentsMap.get(comment.getId())));

        return rootComment;
    }
}
