package com.high.highblog.bloc;

import com.high.highblog.model.entity.User;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserDetailBloc {
    private final UserService userService;

    public UserDetailBloc(final UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public User getUserDetail(String nickName) {
        log.info("get user detail by nick name #{}", nickName);
        return userService.getByNickName(nickName);
    }
}
