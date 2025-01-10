package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CampusException extends RuntimeException{
    private final HttpStatus status;

    public CampusException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class CampusNotFoundException extends  CampusException{
        public CampusNotFoundException() {
            super("존재하지 않는 캠퍼스입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
