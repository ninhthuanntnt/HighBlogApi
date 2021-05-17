package com.high.highblog.bloc.admin;

import com.high.highblog.bloc.notification.NotificationBloc;
import com.high.highblog.enums.NotificationType;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.PostMapper;
import com.high.highblog.model.dto.request.PostCreateReq;
import com.high.highblog.model.dto.request.PostUpdateReq;
import com.high.highblog.model.entity.*;
import com.high.highblog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component("adminPostCrudBloc")
public class PostCrudBloc {

    private final PostService postService;
    private final PostTagService postTagService;
    private final UserService userService;

    private final NotificationBloc notificationBloc;

    public PostCrudBloc(final PostService postService,
                        final PostTagService postTagService,
                        final UserService userService,
                        final NotificationBloc notificationBloc) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.userService = userService;
        this.notificationBloc = notificationBloc;
    }


    @Transactional
    public void deletePost(Long id, String nickName) {
        Long userId = userService.getByNickName(nickName).getId();
        log.info("Delete post by id #{} with userId #{}", id, userId);

        postService.delete(id, userId);
        postTagService.deleteAll(id);

        notificationBloc.deleteNotificationToFollowers(id);
    }
    @Transactional
    public void softDeletePost(Long id, String nickName) {
        Long userId = userService.getByNickName(nickName).getId();
        log.info("Delete post by id #{} with userId #{}", id, userId);

        postService.softDelete(id, userId);

        notificationBloc.deleteNotificationToFollowers(id);
    }
    @Transactional
    public void restorePost(Long id, String nickName) {
        Long userId = userService.getByNickName(nickName).getId();
        log.info("Restore post by id #{} with userId #{}", id, userId);

        postService.restorePost(id, userId);
    }
}
