package com.high.highblog.bloc;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.UserMapper;
import com.high.highblog.model.dto.request.FileReq;
import com.high.highblog.model.dto.request.UserUpdateReq;
import com.high.highblog.model.entity.File;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.FileService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Slf4j
@Component
public class UserCrudBloc {
    private final UserService userService;
    private final FileService fileService;

    public UserCrudBloc(final UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public User getUserDetail(String nickName) {
        log.info("get user detail by nick name #{}", nickName);
        return userService.getByNickName(nickName);
    }

    @Transactional
    public void updateUser(final UserUpdateReq userUpdateReq) {
        Long userId = SecurityHelper.getUserId();
        validateUserUpdateReq(userUpdateReq, userId);

        log.info("Update profile of user detail by id #{}", userId);
        User dbUser = userService.getById(userId);
        User newUser = UserMapper.INSTANCE.toUser(userUpdateReq, dbUser);
        userService.save(newUser);
    }

    private void validateUserUpdateReq(final UserUpdateReq userUpdateReq, Long currentUserId) {
        String newNickname = userUpdateReq.getNickName();
        User currentUser = userService.getById(currentUserId);
        if(newNickname.equals(currentUser.getNickName())) return;
        if (userService.existsByNickName(newNickname))
            throw new ValidatorException("NickName already exists", "nickName");
    }

    public String uploadAvatar(MultipartFile  avatarReq) {
        Long currentUserId = SecurityHelper.getUserId();
        User currentUser = userService.getById(currentUserId);
        String path = fileService.saveNewImageToStorage(avatarReq);

        if(currentUser.getImagePath()!= null){
            fileService.deleteImageFromStorageByPath(currentUser.getImagePath());
        }
        userService.saveAvatar(currentUserId,path);

        return path;
    }
}
