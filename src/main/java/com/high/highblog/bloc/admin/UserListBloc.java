package com.high.highblog.bloc.admin;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component("adminUerListBloc")
public class UserListBloc {
    private final UserService userService;

    public UserListBloc(final UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Page<User> fetchUsers(BasePaginationReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        log.info("Fetch list user");
        return userService.fetchUsers(pageRequest);
    }
    @Transactional(readOnly = true)
    public Long countUsers() {
        log.info("count number of user");
        return userService.countUsers();
    }
}
