package com.high.highblog.model.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.high.highblog.model.dto.response.TagRes;
import com.high.highblog.model.dto.response.UserRes;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminPostRes {

    private Long id;

    @NotNull
    @NotEmpty
    private String title;

    private String summary;

    private String coverImagePath;

    private Long createdDate;

    private Long lastModifiedDate;

    private Long numberOfVotes;

    private Long numberOfComments;

    private Long numberOfFavorites;

    private boolean deleted;

    @JsonProperty("tags")
    private List<TagRes> tagsRes;

    @JsonProperty("user")
    private UserRes userRes;
}
