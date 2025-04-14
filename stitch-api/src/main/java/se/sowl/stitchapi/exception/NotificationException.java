package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotificationException extends RuntimeException {
    private final HttpStatus status;

    public NotificationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static class NotificationNotFoundException extends NotificationException {
        public NotificationNotFoundException() {
            super("알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class UnauthorizedException extends NotificationException {
        public UnauthorizedException() {
            super("해당 알림에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}