package com.high.highblog.bloc.admin;


import com.high.highblog.enums.RoleType;
import com.high.highblog.model.dto.request.admin.RolesUpdateReq;
import com.high.highblog.model.entity.Role;
import com.high.highblog.service.AccountRoleService;
import com.high.highblog.service.AccountService;
import com.high.highblog.service.RoleService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component("adminUserRolesBloc")
public class UserRolesCrudBloc {
    private final UserService userService;
    private final AccountService accountService;
    private final AccountRoleService accountRoleService;
    private final RoleService roleService;

    public UserRolesCrudBloc(UserService userService, AccountService accountService, AccountRoleService accountRoleService, RoleService roleService) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
        this.roleService = roleService;
    }
    @Transactional
    public void updateUserRoles(RolesUpdateReq req) {
        log.info("update role of userId #{}",req.getUserId());
        log.info("roles : #{}",req.getRoleType());
        Long accountId = accountService.getAccountByUserId(req.getUserId()).getId();
        List<Long> roles = new ArrayList<>();
        for (RoleType roleType: req.getRoleType() ) {
            roles.add(roleService.getRoleByRoleType(roleType).getId());
        }
        accountRoleService.deleteOldAndSaveNew(accountId,roles);
    }
}
