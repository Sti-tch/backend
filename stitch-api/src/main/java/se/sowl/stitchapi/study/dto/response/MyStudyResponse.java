package se.sowl.stitchapi.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "내 스터디 응답")
public class MyStudyResponse {
    // 스터디 정보
    @Schema(description = "스터디 게시글 ID", example = "1")
    private Long studyPostId;

    @Schema(description = "스터디 제목", example = "Java 스터디 모집합니다")
    private String studyTitle;

    @Schema(description = "스터디 내용", example = "매주 토요일 오후 2시에 스터디를 진행합니다")
    private String studyContent;

    @Schema(description = "스터디 상태", example = "RECRUITING")
    private StudyStatus studyStatus;

    @Schema(description = "스터디 생성일시")
    private LocalDateTime studyCreatedAt;

    // 내 참여 정보
    @Schema(description = "멤버십 ID", example = "1")
    private Long membershipId;

    @Schema(description = "내 역할", example = "MEMBER")
    private MemberRole myRole;

    @Schema(description = "내 상태", example = "APPROVED")
    private MemberStatus myStatus;

    @Schema(description = "가입일시")
    private LocalDateTime joinedAt;

    // 스터디 작성자 정보
    @Schema(description = "작성자 이름", example = "김영희")
    private String authorName;

    @Schema(description = "작성자 닉네임", example = "영희짱")
    private String authorNickname;

    public static MyStudyResponse from(StudyMember studyMember) {
        return MyStudyResponse.builder()
                .studyPostId(studyMember.getStudyPost().getId())
                .studyTitle(studyMember.getStudyPost().getTitle())
                .studyContent(studyMember.getStudyPost().getContent())
                .studyStatus(studyMember.getStudyPost().getStudyStatus())
                .studyCreatedAt(studyMember.getStudyPost().getCreatedAt())
                .membershipId(studyMember.getId())
                .myRole(studyMember.getMemberRole())
                .myStatus(studyMember.getMemberStatus())
                .joinedAt(studyMember.getCreatedAt())
                .authorName(studyMember.getStudyPost().getUserCamInfo().getUser().getName())
                .authorNickname(studyMember.getStudyPost().getUserCamInfo().getUser().getNickname())
                .build();
    }
}