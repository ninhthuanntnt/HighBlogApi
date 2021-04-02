package com.high.highblog.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.high.highblog.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostVoteRes {
    private Long id;

    private VoteType voteType;
}
