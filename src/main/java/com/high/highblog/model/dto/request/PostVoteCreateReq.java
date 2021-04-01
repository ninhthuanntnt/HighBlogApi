package com.high.highblog.model.dto.request;

import com.high.highblog.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostVoteCreateReq {

    @NotNull
    private Long postId;

    @NotNull
    private VoteType voteType;
}
