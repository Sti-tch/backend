package se.sowl.stitchapi.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.NotificationException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.notification.dto.NotificationListResponse;
import se.sowl.stitchapi.notification.dto.NotificationResponse;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.repository.NotificationRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserCamInfoRepository userCamInfoRepository;

    // 사용자 알림 목록 조회
    @Transactional
    public List<NotificationListResponse> getUserNotifications(Long userCamInfoId){
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        List<Notification> notificationListResponses = notificationRepository
                .findByUserCamInfoOrderByCreatedAtDesc(userCamInfo);

        return notificationListResponses.stream()
                .map(NotificationListResponse::from)
                .toList();
    }

    // 알림 읽음 처리
    @Transactional
    public NotificationResponse markOneAsRead(Long notificationId, Long userCamInfoId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationException.NotificationNotFoundException::new);

        if (!notification.getUserCamInfo().getId().equals(userCamInfoId)){
            throw new NotificationException.UnauthorizedException();
        }

        notification.markAsRead();
        return NotificationResponse.from(notification);
    }

    // 모든 알림 읽음 처리
    @Transactional
    public void markAllAsRead(Long userCamInfoId){
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        List<Notification> unReadNotifications = notificationRepository
                .findByUserCamInfoAndIsReadFalse(userCamInfo);

        unReadNotifications.forEach(Notification::markAsRead);
    }
}
