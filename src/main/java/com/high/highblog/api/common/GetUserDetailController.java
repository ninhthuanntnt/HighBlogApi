package com.high.highblog.api.common;


import com.high.highblog.bloc.UserDetailBloc;
import com.high.highblog.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class GetUserDetailController {

    private final UserDetailBloc userDetailBloc;

    public GetUserDetailController(final UserDetailBloc userDetailBloc) {
        this.userDetailBloc = userDetailBloc;
    }

    @GetMapping("/{nickName}")
    public ResponseEntity<?> getUserDetail(@PathVariable String nickName) {
        User user = userDetailBloc.getUserDetail(nickName);
        return ResponseEntity.ok(user);
    }
}