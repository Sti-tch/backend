package se.sowl.stitchapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.notification.domain.Notification;

@Getter
@AllArgsConstructor
@Builder
public class NotificationListResponse {
    private Long id;
    private String message;
    private String link;
    private boolean isRead;
    private String type;
    private Long targetId;
    private String createdAt;

    public static NotificationListResponse from(Notification notification) {
        return NotificationListResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isRead(notification.isRead())
                .type(notification.getNotificationType() != null ? notification.getNotificationType().toString() : null)
                .targetId(notification.getTargetId())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}
