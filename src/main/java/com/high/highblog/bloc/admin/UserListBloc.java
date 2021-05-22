package com.high.highblog.bloc.admin;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.*;
import com.high.highblog.service.AccountService;
import com.high.highblog.service.RoleService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component("adminUerListBloc")
public class UserListBloc {
    private final UserService userService;
    private final AccountService accountService;
    private final RoleService roleService;

    public UserListBloc(final UserService userService, AccountService accountService, RoleService roleService) {
        this.userService = userService;
        this.accountService = accountService;
        this.roleService = roleService;
    }

    @Transactional(readOnly = true)
    public Page<User> fetchUsers(BasePaginationReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        log.info("Fetch list user");
        Page<User> users = userService.fetchUsers(pageRequest);
        includeUserRoleToUsers(users);
        return users;
    }
    @Transactional(readOnly = true)
    public Long countUsers() {
        log.info("count number of user");
        return userService.countUsers();
    }

    private void includeUserRoleToUsers(Page<User> users) {
        Map<Long, List<Role>> userIdUserRoleMap = new HashMap<>();
        for (User user: users ) {
            Long  accountId = accountService.getAccountByUserId(user.getId()).getId();
            List<Role> roles = roleService.fetchByAccountId(accountId);
            userIdUserRoleMap.put(user.getId(),roles);
        }
        users.forEach(user -> user.setRoles(userIdUserRoleMap.get(user.getId())));
    }
}
