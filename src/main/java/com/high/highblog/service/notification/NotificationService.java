package com.high.highblog.service.notification;

import com.high.highblog.enums.NotificationType;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Notification;
import com.high.highblog.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(final NotificationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Notification> fetchByReceiverIdWithPageRequest(final Long receiverId,
                                                               final PageRequest pageRequest) {
        log.info("Fetch list notifications");

        return repository.findByReceiverId(receiverId, pageRequest);
    }

    @Transactional
    public Notification saveNew(final Notification notification) {
        log.info("Save new notification with data #{}", notification);

        validateBeforeSaveNew(notification);

        return repository.save(notification);
    }

    @Transactional
    public void softDeleteBySourceId(final Long sourceId) {
        log.info("Soft delete by source id #{}", sourceId);

        Notification notification = repository.findBySourceId(sourceId)
                                              .orElseThrow(() -> new ObjectNotFoundException("notification"));
        notification.setDeleted(true);

        repository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> fetchUnreadNotifications(final Long userId) {
        log.info("Fetch unread notifications by userId #{}", userId);

        return repository.findByUserIdAndSeen(userId, false);
    }

    private void validateBeforeSaveNew(final Notification notification) {
        if (ObjectUtils.isEmpty(notification))
            throw new ValidatorException("Should be not null", "notification");
        if (notification.getId() != null && notification.getId() != 0)
            throw new ValidatorException("Invalid id", "notification");
    }


    public Page<Notification> fetchAdminNotifications(NotificationType type, PageRequest pageRequest) {
        log.info("fetch list notification with type #{}",type);
        return repository.findNotificationsByType(type,pageRequest);
    }
}
