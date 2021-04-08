package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.User;
import com.high.highblog.repository.UserDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserDetailService {
    private final UserDetailRepository userDetailRepository;

    public UserDetailService(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }
    @Transactional(readOnly = true)
    public User findById(long id){
        log.info("Get user by id");
        User user = userDetailRepository.findById(id);
        return user;
    }
}
