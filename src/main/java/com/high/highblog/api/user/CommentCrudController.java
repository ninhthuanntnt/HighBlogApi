package com.high.highblog.api.user;

import com.high.highblog.bloc.CommentBloc;
import com.high.highblog.model.dto.request.CommentCreateReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/comments")
public class CommentCrudController {

    private final CommentBloc commentBloc;

    public CommentCrudController(final CommentBloc commentBloc) {
        this.commentBloc = commentBloc;
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody final CommentCreateReq commentCreateReq) {

        commentBloc.createCommentForCurrentUser(commentCreateReq);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
