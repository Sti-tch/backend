package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StudyPostException extends RuntimeException {
    private final HttpStatus status;

    public StudyPostException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static class StudyPostNotFoundException extends StudyPostException {
        public StudyPostNotFoundException() {
            super("존재하지 않는 스터디 게시글입니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class UnauthorizedException extends StudyPostException {
        public UnauthorizedException() {
            super("해당 스터디 게시글에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    public static class StudyPostCommentNotFoundException extends StudyPostException {
        public StudyPostCommentNotFoundException() {super("존재하지 않는 스터디 게시글 댓글입니다.", HttpStatus.NOT_FOUND);}
    }
}
