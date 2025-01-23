package se.sowl.stitchapi.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import se.sowl.stitchdomain.user.domain.User;

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
}
