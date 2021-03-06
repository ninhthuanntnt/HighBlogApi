package com.high.highblog.bloc;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.response.ProfileRes;
import com.high.highblog.model.entity.Role;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.RoleService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProfileBloc {

    private final UserService userService;
    private final RoleService roleService;

    public ProfileBloc(final UserService userService,
                       final RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public ProfileRes getProfileResForCurrentUser() {
        Long userId = SecurityHelper.getUserId();
        Long accountId = SecurityHelper.getAccountId();

        User user = userService.getById(userId);
        List<Role> roles = roleService.fetchByAccountId(accountId);

        return ProfileRes.builder()
                         .firstName(user.getFirstName())
                         .lastName(user.getLastName())
                         .imagePath(user.getImagePath())
                         .roleTypes(roles.stream().map(Role::getRoleType).collect(Collectors.toList()))
                         .build();
    }

}
