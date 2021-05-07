package com.high.highblog.api.common;


import com.high.highblog.bloc.UserCrudBloc;
import com.high.highblog.mapper.UserMapper;
import com.high.highblog.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserCrudController {

    private final UserCrudBloc userCrudBloc;

    public UserCrudController(final UserCrudBloc userCrudBloc) {
        this.userCrudBloc = userCrudBloc;
    }

    @GetMapping("/{nickName}")
    public ResponseEntity<?> getUserDetail(@PathVariable String nickName) {
        User user = userCrudBloc.getUserDetail(nickName);
        return ResponseEntity.ok(UserMapper.INSTANCE.toUserRes(user));
    }
}