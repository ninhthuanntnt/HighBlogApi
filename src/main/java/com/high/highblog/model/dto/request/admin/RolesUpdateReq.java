package com.high.highblog.model.dto.request.admin;

import com.high.highblog.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RolesUpdateReq {
    private Long userId;
    private List<RoleType> roleType;
}
