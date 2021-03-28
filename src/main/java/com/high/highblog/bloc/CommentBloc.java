package com.high.highblog.bloc;

import com.high.highblog.model.entity.Comment;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.CommentService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

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

    public List<Comment> fetchCommentsByPostId(final Long postId) {
        log.info("Fetch comments by post id #{}", postId);
        List<Comment> comments = commentService.fetchByPostId(postId);

        includeUserToComments(comments);

        return populateComments(comments);
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
                        .collect(Collectors.groupingBy(Comment::getParentId));


        rootComment.forEach(comment -> comment.setChildComments(childCommentsMap.get(comment.getId())));

        return rootComment;
    }
}
