package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.User;
import com.high.highblog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        log.info("Get user by id");

        return userRepository.findById(id)
                             .orElseThrow(() -> new ObjectNotFoundException("user"));
    }

    @Transactional
    public void save(final User user) {
        log.info("Save user with info #{}", user);

        userRepository.save(user);
    }
}
