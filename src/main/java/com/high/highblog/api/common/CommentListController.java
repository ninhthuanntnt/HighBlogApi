package com.high.highblog.api.common;

import com.high.highblog.bloc.CommentBloc;
import com.high.highblog.mapper.CommentMapper;
import com.high.highblog.model.entity.Comment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentListController {

    private final CommentBloc commentBloc;

    public CommentListController(final CommentBloc commentBloc) {
        this.commentBloc = commentBloc;
    }

    @GetMapping
    public ResponseEntity<?> fetchListCommentForPost(@RequestParam Long postId) {

        List<Comment> comments = commentBloc.fetchCommentsByPostId(postId);
        return ResponseEntity.ok(CommentMapper.INSTANCE.toListCommentsRes(comments));
    }
}
