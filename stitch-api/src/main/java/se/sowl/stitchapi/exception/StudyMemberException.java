package se.sowl.stitchapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StudyMemberException extends RuntimeException {
    private final HttpStatus status;

    public StudyMemberException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static class MemberNotFoundException extends StudyMemberException {
        public MemberNotFoundException() {
            super("스터디 멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class AlreadyAppliedOrMemberException extends StudyMemberException {
        public AlreadyAppliedOrMemberException() {
            super("이미 가입 신청했거나 멤버입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotLeaderException extends StudyMemberException {
        public NotLeaderException() {
            super("스터디 리더만 이 작업을 수행할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
    }

    public static class LeaderNotFoundException extends StudyMemberException {
        public LeaderNotFoundException() {
            super("스터디 리더를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class AlreadyProcessedException extends StudyMemberException {
        public AlreadyProcessedException() {
            super("이미 처리된 가입 신청입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotMemberException extends StudyMemberException {
        public NotMemberException() {
            super("스터디 멤버가 아닙니다.", HttpStatus.FORBIDDEN);
        }
    }

    public static class LeaderCannotLeaveException extends StudyMemberException {
        public LeaderCannotLeaveException() {
            super("스터디 리더는 탈퇴할 수 없습니다. 먼저 리더 권한을 다른 멤버에게 양도하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotApprovedMemberException extends StudyMemberException {
        public NotApprovedMemberException() {
            super("승인된 멤버만 이 작업을 수행할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
    }

    public static class SchoolVerificationRequiredException extends StudyMemberException {
        public SchoolVerificationRequiredException() {
            super("스터디 가입을 위해서는 학교 인증이 필요합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}