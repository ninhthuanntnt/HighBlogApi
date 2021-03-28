package com.high.highblog.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CommentRes {

    private Long id;

    private String content;

    private Long numberOfVotes;

    @JsonProperty("childComments")
    private List<CommentRes> childCommentsRes;

    @JsonProperty("user")
    private UserRes userRes;
}
