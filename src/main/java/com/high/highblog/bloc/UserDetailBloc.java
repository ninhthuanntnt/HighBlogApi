package com.high.highblog.bloc;

import com.high.highblog.model.entity.Category;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.CategoryService;
import com.high.highblog.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class UserDetailBloc {
    private final UserDetailService userDetailService;

    public UserDetailBloc(final UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Transactional(readOnly = true)
    public User getUserDetail(long id) {
        log.info("get user detail");

        return userDetailService.findById(id);
    }
}
