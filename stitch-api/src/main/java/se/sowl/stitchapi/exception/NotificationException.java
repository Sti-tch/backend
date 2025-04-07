package se.sowl.stitchapi.exception;

public class NotificationException {

    public static class NotificationNotFoundException extends RuntimeException {
        public NotificationNotFoundException() {
            super("알림을 찾을 수 없습니다.");
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException() {
            super("해당 알림에 접근 권한이 없습니다.");
        }
    }
}