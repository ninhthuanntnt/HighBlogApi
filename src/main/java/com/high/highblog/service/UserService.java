package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.User;
import com.high.highblog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Collection<User> fetchByIdIn(final Collection<Long> ids){
        log.info("Fetch list users by id in #{}", ids);

        if(ObjectUtils.isEmpty(ids))
            return Collections.emptyList();

        return userRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        log.info("Get user by id #{}", id);

        return userRepository.findById(id)
                             .orElseThrow(() -> new ObjectNotFoundException("user"));
    }

    @Transactional(readOnly = true)
    public User getByNickName(final String nickName) {
        log.info("Get user by id");

        return userRepository.findByNickName(nickName)
                             .orElseThrow(() -> new ObjectNotFoundException("user"));
    }

    @Transactional
    public void saveNew(final User user) {
        log.info("Save new user with info #{}", user);

        validateUserBeforeSaveNew(user);

        userRepository.save(user);
    }

    private void validateUserBeforeSaveNew(final User user) {
        if (ObjectUtils.isNotEmpty(user.getId())) {
            throw new ValidatorException("Invalid user id", "id");
        }
    }
}
