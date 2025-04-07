package se.sowl.stitchapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.notification.domain.Notification;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private Long userCamInfoId;
    private String message;
    private String link;
    private boolean isRead;
    private String notificationType;
    private Long targetId;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userCamInfoId(notification.getUserCamInfo().getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isRead(notification.isRead())
                .notificationType(notification.getNotificationType().name())
                .targetId(notification.getTargetId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
