package se.sowl.stitchapi.study.dto.response;

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
public class MyStudyResponse {
    // 스터디 정보
    private Long studyPostId;
    private String studyTitle;
    private String studyContent;
    private StudyStatus studyStatus;
    private LocalDateTime studyCreatedAt;

    // 내 참여 정보
    private Long membershipId;
    private MemberRole myRole;
    private MemberStatus myStatus;
    private LocalDateTime joinedAt;

    // 스터디 작성자 정보
    private String authorName;
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
