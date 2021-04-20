package com.high.highblog.api.common;

import com.high.highblog.bloc.CommentListBloc;
import com.high.highblog.mapper.CommentMapper;
import com.high.highblog.model.dto.request.CommentListReq;
import com.high.highblog.model.entity.Comment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentListController {

    private final CommentListBloc commentListBloc;

    public CommentListController(final CommentListBloc commentListBloc) {
        this.commentListBloc = commentListBloc;
    }

    @GetMapping
    public ResponseEntity<?> fetchListCommentForPost(final CommentListReq commentListReq) {

        List<Comment> comments = commentListBloc.fetchCommentsByPostId(commentListReq);
        return ResponseEntity.ok(CommentMapper.INSTANCE.toListCommentsRes(comments));
    }
}
