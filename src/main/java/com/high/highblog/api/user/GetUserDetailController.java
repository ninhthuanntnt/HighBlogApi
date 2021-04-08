package com.high.highblog.api.user;

import com.high.highblog.bloc.CategoryListBloc;
import com.high.highblog.bloc.UserDetailBloc;
import com.high.highblog.mapper.CategoryMapper;
import com.high.highblog.model.dto.response.CategoryRes;
import com.high.highblog.model.entity.Category;
import com.high.highblog.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/detail")
public class GetUserDetailController {

    private final UserDetailBloc userDetailBloc;

    public GetUserDetailController(final UserDetailBloc userDetailBloc) {
        this.userDetailBloc = userDetailBloc;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetail(@PathVariable Long id) {
        User user = userDetailBloc.getUserDetail(id);
        return ResponseEntity.ok(user);
    }
}
