package com.high.highblog.api.user;

import com.high.highblog.bloc.PostVoteCrudBloc;
import com.high.highblog.model.dto.request.PostVoteCreateReq;
import com.high.highblog.model.dto.request.PostVoteUpdateReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/posts-votes")
public class PostVoteCrudController {

    private final PostVoteCrudBloc postVoteCrudBloc;

    public PostVoteCrudController(final PostVoteCrudBloc postVoteCrudBloc) {
        this.postVoteCrudBloc = postVoteCrudBloc;
    }

    @PostMapping
    public ResponseEntity<Long> createPostVote(@RequestBody PostVoteCreateReq postVoteCreateReq) {

        postVoteCrudBloc.createPostVoteForCurrentUser(postVoteCreateReq);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> updatePostVote(@RequestBody final PostVoteUpdateReq postVoteUpdateReq) {

        postVoteCrudBloc.updatePostVoteForCurrentUser(postVoteUpdateReq);

        return ResponseEntity.noContent().build();
    }
}
