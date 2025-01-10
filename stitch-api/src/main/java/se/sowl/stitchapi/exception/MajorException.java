package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MajorException extends RuntimeException{
    private final HttpStatus status;

    public MajorException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class MajorNotFoundException extends  MajorException{
        public MajorNotFoundException() {
            super("존재하지 않는 전공입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
