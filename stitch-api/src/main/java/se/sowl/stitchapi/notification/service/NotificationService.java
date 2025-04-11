package se.sowl.stitchapi.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.NotificationException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.notification.dto.NotificationListResponse;
import se.sowl.stitchapi.notification.dto.NotificationResponse;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.enumm.NotificationType;
import se.sowl.stitchdomain.notification.repository.NotificationRepository;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
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
    public List<NotificationListResponse> getUserNotifications(Long userCamInfoId) {
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
    public NotificationResponse markOneAsRead(Long notificationId, Long userCamInfoId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationException.NotificationNotFoundException::new);

        if (!notification.getUserCamInfo().getId().equals(userCamInfoId)) {
            throw new NotificationException.UnauthorizedException();
        }

        notification.markAsRead();
        return NotificationResponse.from(notification);
    }

    // 모든 알림 읽음 처리
    @Transactional
    public void markAllAsRead(Long userCamInfoId) {
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        List<Notification> unReadNotifications = notificationRepository
                .findByUserCamInfoAndIsReadFalse(userCamInfo);

        unReadNotifications.forEach(Notification::markAsRead);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotification(Long notificationId, Long userCamInfoId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationException.NotificationNotFoundException::new);

        if (!notification.getUserCamInfo().getId().equals(userCamInfoId)) {
            throw new NotificationException.UnauthorizedException();
        }
        notificationRepository.delete(notification);
    }

    // 알림 생성(스터디 가입 신청)
    // usercaminfo는 알림을 받는 당사자
    @Transactional
    public NotificationResponse createStudyApplyNotification(UserCamInfo receiver, StudyMember studyMember) {
        Notification notification = Notification.builder()
                .userCamInfo(receiver) // 알림 받는 사람: 스터디 관리자(리더)
                .message(studyMember.getUserCamInfo().getUser().getName() +
                        "님이 '" + studyMember.getStudyPost().getTitle() + "' 스터디에 가입 신청했습니다.")
                .link("/api/studies/" + studyMember.getStudyPost().getId() + "/members")
                .notificationType(NotificationType.STUDY_APPLY)
                .targetId(studyMember.getId())
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        return NotificationResponse.from(notification);
    }

    // 알림 생성(스터디 가입 승인)
    @Transactional
    public NotificationResponse createApproveNotification(StudyMember studyMember) {
        Notification notification = Notification.builder()
                .userCamInfo(studyMember.getUserCamInfo()) // 알림 받는 당사자: 신청자
                .message("'" + studyMember.getStudyPost().getTitle() + "' 스터디 가입이 승인되었습니다.")
                .link("/api/studies/" + studyMember.getStudyPost().getId())
                .notificationType(NotificationType.STUDY_APPLY_APPROVED)
                .targetId(studyMember.getStudyPost().getId())
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        return NotificationResponse.from(notification);
    }

    // 알림 생성(스터디 가입 거절)
    @Transactional
    public NotificationResponse createRejectNotification(StudyMember studyMember) {
        Notification notification = Notification.builder()
                .userCamInfo(studyMember.getUserCamInfo()) // 알림 받는 당사자: 신청자
                .message("'" + studyMember.getStudyPost().getTitle() + "' 스터디 가입이 거절되었습니다.")
                .link("/api/studies/" + studyMember.getStudyPost().getId())
                .notificationType(NotificationType.STUDY_APPLY_REJECTED)
                .targetId(studyMember.getStudyPost().getId())
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        return NotificationResponse.from(notification);
    }

    // 알림 생성(새 댓글)
    @Transactional
    public NotificationResponse createNewCommentNotification(StudyPost studyPost, UserCamInfo commentWriter) {
        // 게시글 작성자에게만 알림 (자신의 글에 자신이 댓글을 달면 알림 제외)
        if (!studyPost.getUserCamInfo().getId().equals(commentWriter.getId())) {
            Notification notification = Notification.builder()
                    .userCamInfo(studyPost.getUserCamInfo()) // 알림 받는 당사자: 게시글 작성자
                    .message(commentWriter.getUser().getName() +
                            "님이 '" + studyPost.getTitle() + "' 게시글에 댓글을 남겼습니다.")
                    .link("/api/studies/" + studyPost.getId() + "/comments")
                    .notificationType(NotificationType.STUDY_COMMENT_ADDED)
                    .targetId(studyPost.getId())
                    .isRead(false)
                    .build();

            notification = notificationRepository.save(notification);
            return NotificationResponse.from(notification);
        }

        return null; // 자신의 글에 자신이 댓글을 달면 알림 없음
    }
}