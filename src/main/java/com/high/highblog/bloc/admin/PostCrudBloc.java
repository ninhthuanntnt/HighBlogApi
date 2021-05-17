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

    private final NotificationBloc notificationBloc;

    public PostCrudBloc(final PostService postService,
                        final PostTagService postTagService,
                        final NotificationBloc notificationBloc) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.notificationBloc = notificationBloc;
    }


    @Transactional
    public void deletePost(final Long id) {
        log.info("Delete post by id #{} ", id);

        postService.delete(id);
        postTagService.deleteAll(id);

        notificationBloc.deleteNotificationToFollowers(id);
    }
    @Transactional
    public void softDeletePost(final Long id) {
        log.info("Delete post by id #{}", id);

        postService.softDelete(id);

        notificationBloc.deleteNotificationToFollowers(id);
    }
    @Transactional
    public void restorePost(final Long id) {
        log.info("Restore post by id #{}", id);

        postService.restorePost(id);
    }
}
