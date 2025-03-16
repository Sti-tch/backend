package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserCamInfoException extends RuntimeException{
    private final HttpStatus status;

    public UserCamInfoException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class UserCamNotFoundException extends UserCamInfoException {
        public UserCamNotFoundException() {
            super("존재하지 않는 캠퍼스 인증자 정보입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
