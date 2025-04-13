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
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("commentWriterId") Long commentWriterId
    ){
        NotificationResponse response = notificationService.createNewCommentNotification(studyPostId, commentWriterId);
        return CommonResponse.ok(response);
    }
}
