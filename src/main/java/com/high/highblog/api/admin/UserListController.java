package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.UserListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.UserMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin/users")
public class UserListController {
    private final UserListBloc userListBloc;

    public UserListController(UserListBloc userListBloc) {
        this.userListBloc = userListBloc;
    }

    @GetMapping
    public ResponseEntity<?> fetchUsers(BasePaginationReq basePaginationReq) {
        Page<User> users = userListBloc.fetchUsers(basePaginationReq);
        return ResponseEntity.ok(PaginationHelper
                .buildBasePaginationRes(users.map(UserMapper.INSTANCE::toAdminUserRes)));
    }
}
