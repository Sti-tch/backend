package se.sowl.stitchapi.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.notification.dto.NotificationListResponse;
import se.sowl.stitchapi.notification.dto.NotificationResponse;
import se.sowl.stitchapi.notification.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/list")
    public CommonResponse<List<NotificationListResponse>> getUserNotifications(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        List<NotificationListResponse> responses = notificationService.getUserNotifications(userCamInfoId);
        return CommonResponse.ok(responses);
    }

    @GetMapping("/unread-count")
    public CommonResponse<Integer> getUnreadNotificationCount(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        int count = notificationService.getUnreadNotificationCount(userCamInfoId);
        return CommonResponse.ok(count);
    }

    @GetMapping("/detail")
    public CommonResponse<NotificationResponse> getNotificationDetail(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        NotificationResponse response = notificationService.getNotificationDetail(notificationId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/read")
    public CommonResponse<NotificationResponse> markOneRead(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        NotificationResponse response = notificationService.markOneAsRead(notificationId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/read/all")
    public CommonResponse<Void> markAllRead(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        notificationService.markAllAsRead(userCamInfoId);
        return CommonResponse.ok(null);
    }

    @DeleteMapping("/delete")
    public CommonResponse<Void> deleteNotification(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        notificationService.deleteNotification(notificationId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    @PostMapping("/study-apply")
    public CommonResponse<NotificationResponse> createStudyApplyNotification(
            @RequestParam("receiverId") Long receiverId,
            @RequestParam("studyMemberId") Long studyMemberId
    ){
        NotificationResponse response = notificationService.createStudyApplyNotification(receiverId, studyMemberId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/study-approve")
    public CommonResponse<NotificationResponse> createStudyApproveNotification(
            @RequestParam("studyMemberId") Long studyMemberId
    ){
        NotificationResponse response = notificationService.createApproveNotification(studyMemberId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/study-reject")
    public CommonResponse<NotificationResponse> createStudyRejectNotification(
            @RequestParam("studyMemberId") Long studyMemberId
    ){
        NotificationResponse response = notificationService.createRejectNotification(studyMemberId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/study-comment")
    public CommonResponse<NotificationResponse> createNewCommentNotification(
            @RequestParam("commentId") Long commentId
    ){
        NotificationResponse response = notificationService.createNewCommentNotification(commentId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/study-content")
    public CommonResponse<List<NotificationResponse>> createNewContentNotification(
            @RequestParam("studyContentId") Long studyContentId,
            @RequestParam("contentWriterId") Long contentWriterId
    ){
        List<NotificationResponse> response = notificationService.createNewContentNotification(studyContentId, contentWriterId);
        return CommonResponse.ok(response);
    }
}
