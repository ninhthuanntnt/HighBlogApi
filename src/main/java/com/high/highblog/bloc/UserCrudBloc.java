package com.high.highblog.bloc;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.UserMapper;
import com.high.highblog.model.dto.request.UserUpdateReq;
import com.high.highblog.model.entity.Subscription;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.FileService;
import com.high.highblog.service.SubscriptionService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Component
public class UserCrudBloc {
    private final UserService userService;
    private final FileService fileService;
    private final SubscriptionService subscriptionService;

    public UserCrudBloc(final UserService userService,
                        final FileService fileService,
                        final SubscriptionService subscriptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional(readOnly = true)
    public User getUserDetail(String nickName) {
        log.info("get user detail by nick name #{}", nickName);

        User user = userService.getByNickName(nickName);
        user.setNumberOfFollowers(subscriptionService.getNumberOfFollowersByUserId(user.getId()));
        includeExtraInfoIfUserLogined(user);

        return user;
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
    @Transactional
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
    @Transactional
    public String updateBackground(MultipartFile backgroundReq) {
        Long currentUserId = SecurityHelper.getUserId();
        User currentUser = userService.getById(currentUserId);
        String path = fileService.saveNewImageToStorage(backgroundReq);

        if(currentUser.getBackgroundPath()!= null){
            fileService.deleteImageFromStorageByPath(currentUser.getBackgroundPath());
        }
        userService.saveBackground(currentUserId,path);

        return path;
    }

    private void includeExtraInfoIfUserLogined(final User user) {
        try {
            Long currentUserId = SecurityHelper.getUserId();
            if(currentUserId != user.getId()){
                Subscription subscription = subscriptionService.findNullableByUserIdAndFollowerId(user.getId(),
                                                                                                  currentUserId);

                if(ObjectUtils.isNotEmpty(subscription)){
                    user.setFollowed(ObjectUtils.isNotEmpty(subscription));
                    user.setNotified(subscription.isNotified());
                }
            }

        } catch (Exception ex) {
            log.error("Extra info of post detail is not set");
            log.error(ex.getMessage());
        }
    }

}
