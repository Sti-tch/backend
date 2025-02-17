package se.sowl.stitchapi.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException{
    private final HttpStatus status;

    public UserException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class UserNotFoundException extends  UserException{
        public UserNotFoundException() {
            super("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class UserAlreadyCertifiedException extends  UserException{
        public UserAlreadyCertifiedException() {
            super("이미 인증된 사용자입니다.", HttpStatus.CONFLICT);
        }
    }

    public static class UserNotCertifiedException extends UserException {
        public UserNotCertifiedException() {
            super("인증되지 않은 사용자입니다.", HttpStatus.FORBIDDEN);
        }
    }

    public static class CampusNotFoundException extends UserException {
        public CampusNotFoundException() {
            super("존재하지 않는 캠퍼스입니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class InvalidCampusEmailDomainException extends UserException {
        public InvalidCampusEmailDomainException() {
            super("유효하지 않은 대학교 이메일 도메인입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidEmailFormatException extends UserException {
        public InvalidEmailFormatException() {
            super("잘못된 이메일 형식입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class UserCamInfoNotFoundException extends UserException {
        public UserCamInfoNotFoundException() {
            super("존재하지 않는 사용자 캠퍼스 정보입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
