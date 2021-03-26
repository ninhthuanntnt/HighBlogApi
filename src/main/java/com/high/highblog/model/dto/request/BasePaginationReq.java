package com.high.highblog.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BasePaginationReq {

    private Integer page;

    @JsonProperty("page_size")
    private Integer pageSize;

    private String[] sorts;
}
