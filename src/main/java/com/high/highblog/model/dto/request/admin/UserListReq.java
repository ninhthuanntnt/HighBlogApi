package com.high.highblog.model.dto.request.admin;

import com.high.highblog.model.dto.request.BasePaginationReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserListReq extends BasePaginationReq {
    private String nickName;
}
