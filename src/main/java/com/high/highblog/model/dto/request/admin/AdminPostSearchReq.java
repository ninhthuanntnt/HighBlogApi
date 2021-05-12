package com.high.highblog.model.dto.request.admin;

import com.high.highblog.model.dto.request.BasePaginationReq;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminPostSearchReq extends BasePaginationReq {
    private String nickName;
    private List<Long> tagIds;
    private String keyWord;
}
