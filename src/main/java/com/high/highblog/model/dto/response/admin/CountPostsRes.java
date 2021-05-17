package com.high.highblog.model.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountPostsRes {
    private Long total;
    private Long posts;
    private Long questions;
}
