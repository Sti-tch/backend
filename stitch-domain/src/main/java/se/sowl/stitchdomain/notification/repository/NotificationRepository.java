package se.sowl.stitchdomain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.enumm.NotificationType;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 사용자 알림 목록 조회
    List<Notification> findByUserCamInfoOrderByCreatedAtDesc(UserCamInfo userCamInfo);

    // 알림 읽음 처리
    List<Notification> findByUserCamInfoAndIsReadFalse(UserCamInfo userCamInfo);

    Optional<Notification> findFirstByTargetIdAndNotificationTypeOrderByCreatedAtDesc(
            Long targetId, NotificationType notificationType);

    // 알림 읽음 처리를 위한 메소드
    int countByUserCamInfoAndIsReadFalse(UserCamInfo userCamInfo);
}
