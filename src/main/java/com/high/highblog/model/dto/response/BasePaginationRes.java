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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasePaginationRes {

    @JsonProperty("total_items")
    private Long totalItems;

    @JsonProperty("last_page")
    private Integer lastPage;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page")
    private Integer page;

    private List<?> items;
}
