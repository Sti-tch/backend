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

    public static class MajorAlreadySelectedException extends MajorException{
        public MajorAlreadySelectedException() {
            super("이미 전공이 선택되어 있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class MajorIdRequiredException extends MajorException {
        public MajorIdRequiredException() {
            super("전공 ID가 필요합니다.", HttpStatus.BAD_REQUEST);
        }
    }

}
