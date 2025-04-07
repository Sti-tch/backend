package se.sowl.stitchapi.exception;

public class StudyMemberException {

    public static class MemberNotFoundException extends RuntimeException {
        public MemberNotFoundException() {
            super("스터디 멤버를 찾을 수 없습니다.");
        }
    }

    public static class AlreadyAppliedOrMemberException extends RuntimeException {
        public AlreadyAppliedOrMemberException() {
            super("이미 가입 신청했거나 멤버입니다.");
        }
    }

    public static class NotLeaderException extends RuntimeException {
        public NotLeaderException() {
            super("스터디 리더만 이 작업을 수행할 수 있습니다.");
        }
    }

    public static class LeaderNotFoundException extends RuntimeException {
        public LeaderNotFoundException() {
            super("스터디 리더를 찾을 수 없습니다.");
        }
    }

    public static class AlreadyProcessedException extends RuntimeException {
        public AlreadyProcessedException() {
            super("이미 처리된 가입 신청입니다.");
        }
    }

    public static class NotMemberException extends RuntimeException {
        public NotMemberException() {
            super("스터디 멤버가 아닙니다.");
        }
    }

    public static class LeaderCannotLeaveException extends RuntimeException {
        public LeaderCannotLeaveException() {
            super("스터디 리더는 탈퇴할 수 없습니다. 먼저 리더 권한을 다른 멤버에게 양도하세요.");
        }
    }

    public static class NotApprovedMemberException extends RuntimeException {
        public NotApprovedMemberException() {
            super("승인된 멤버만 이 작업을 수행할 수 있습니다.");
        }
    }
}