package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StudyContentException extends RuntimeException{

    private final HttpStatus status;

    public StudyContentException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class ContentNotFoundException extends StudyContentException{
        public ContentNotFoundException() {
            super("존재하지 않는 스터디 콘텐츠입니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class UnauthorizedException extends StudyContentException{
        public UnauthorizedException() {
            super("스터디 콘텐츠에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    public static class InvalidContentTypeException extends RuntimeException {
        public InvalidContentTypeException() {
            super("유효하지 않은 컨텐츠 타입입니다.");
        }
    }
}
