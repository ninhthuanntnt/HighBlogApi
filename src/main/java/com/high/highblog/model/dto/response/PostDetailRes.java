package com.high.highblog.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.high.highblog.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDetailRes {
    private String title;

    private String content;

    private String coverImagePath;

    private Long createdDate;

    private Long lastModifiedDate;

    private String numberOfVotes;

    private Boolean addedToFavorite;

    @JsonProperty("tags")
    private List<TagRes> tagsRes;

    @JsonProperty("vote")
    private PostVoteRes postVoteRes;

    @JsonProperty("user")
    private UserRes userRes;
}
