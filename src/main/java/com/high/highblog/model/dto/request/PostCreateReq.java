package com.high.highblog.model.dto.request;

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
public class PostCreateReq {

    @NotNull
    @JsonProperty("category_id")
    private Long categoryId;

    @NotNull
    @NotEmpty
    private String title;

    private String summary;

    @NotNull
    @NotEmpty
    private String content;

    @JsonProperty("cover_image_path")
    private String coverImagePath;

    @NotNull
    @JsonProperty("post_type")
    private PostType postType;

    @JsonProperty("tags")
    private List<TagCreateReq> tagCreateReqs;
}
