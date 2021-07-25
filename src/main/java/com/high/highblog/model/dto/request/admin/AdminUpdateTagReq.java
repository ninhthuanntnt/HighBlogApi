package com.high.highblog.model.dto.request.admin;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminUpdateTagReq {
    private Long id;
    private String tagName;
}
