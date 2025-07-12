package se.sowl.stitchapi.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "스터디 멤버 응답")
public class StudyMemberResponse {
    @Schema(description = "멤버 ID", example = "1")
    private Long id;

    @Schema(description = "스터디 게시글 ID", example = "1")
    private Long studyPostId;

    @Schema(description = "스터디 제목", example = "Java 스터디 모집합니다")
    private String studyTitle;

    @Schema(description = "사용자 캠퍼스 정보 ID", example = "1")
    private Long userCamInfoId;

    @Schema(description = "사용자 이름", example = "김철수")
    private String userName;

    @Schema(description = "사용자 닉네임", example = "철수짱")
    private String userNickname;

    @Schema(description = "멤버 역할", example = "MEMBER")
    private MemberRole memberRole;

    @Schema(description = "멤버 상태", example = "APPROVED")
    private MemberStatus memberStatus;

    @Schema(description = "신청 메시지", example = "열정적으로 참여하겠습니다!")
    private String applyMessage;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    @Schema(description = "캠퍼스명", example = "서울캠퍼스")
    private String campusName;

    @Schema(description = "전공명", example = "컴퓨터공학과")
    private String majorName;

    public static StudyMemberResponse from(StudyMember studyMember) {
        return StudyMemberResponse.builder()
                .id(studyMember.getId())
                .studyPostId(studyMember.getStudyPost().getId())
                .studyTitle(studyMember.getStudyPost().getTitle())
                .userCamInfoId(studyMember.getUserCamInfo().getId())
                .userName(studyMember.getUserCamInfo().getUser().getName())
                .userNickname(studyMember.getUserCamInfo().getUser().getNickname())
                .memberRole(studyMember.getMemberRole())
                .memberStatus(studyMember.getMemberStatus())
                .applyMessage(studyMember.getApplyMessage())
                .createdAt(studyMember.getCreatedAt())
                .updatedAt(studyMember.getUpdatedAt())
                .campusName(studyMember.getUserCamInfo().getCampus().getName())
                .majorName(studyMember.getUserCamInfo().getMajor() != null ?
                        studyMember.getUserCamInfo().getMajor().getName() : null)
                .build();
    }
}