package com.high.highblog.model.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.high.highblog.model.entity.AccountRole;
import com.high.highblog.model.entity.Role;
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
public class AdminUserRes {

    private Long id;

    private String nickName;

    private String firstName;

    private String lastName;

    private String imagePath;

    private String backgroundPath;

    private List<Role> roles;

}
