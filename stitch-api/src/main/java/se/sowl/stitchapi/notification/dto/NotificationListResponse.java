package se.sowl.stitchapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.enumm.NotificationType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NotificationListResponse {
    private Long id;
    private String message;
    private String link;
    private boolean isRead;
    private NotificationType type;
    private Long targetId;
    private LocalDateTime createdAt;

    public static NotificationListResponse from(Notification notification) {
        return NotificationListResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isRead(notification.isRead())
                .type(notification.getNotificationType())
                .targetId(notification.getTargetId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
