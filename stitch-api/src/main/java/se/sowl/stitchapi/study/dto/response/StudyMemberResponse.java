package se.sowl.stitchapi.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.study.domain.StudyMember;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class StudyMemberResponse {
    private Long id;
    private Long studyPostId;
    private String studyTitle;
    private Long userCamInfoId;
    private String userName;
    private String userNickname;
    private String memberRole;
    private String memberStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String campusName;
    private String majorName;

    public static StudyMemberResponse from(StudyMember studyMember) {
        return StudyMemberResponse.builder()
                .id(studyMember.getId())
                .studyPostId(studyMember.getStudyPost().getId())
                .studyTitle(studyMember.getStudyPost().getTitle())
                .userCamInfoId(studyMember.getUserCamInfo().getId())
                .userName(studyMember.getUserCamInfo().getUser().getName())
                .userNickname(studyMember.getUserCamInfo().getUser().getNickname())
                .memberRole(studyMember.getMemberRole().name())
                .memberStatus(studyMember.getMemberStatus().name())
                .createdAt(studyMember.getCreatedAt())
                .updatedAt(studyMember.getUpdatedAt())
                .campusName(studyMember.getUserCamInfo().getCampus().getName())
                .majorName(studyMember.getUserCamInfo().getMajor() != null ?
                        studyMember.getUserCamInfo().getMajor().getName() : null)
                .build();
    }
}