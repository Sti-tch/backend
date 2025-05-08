package se.sowl.stitchapi.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.study.domain.StudyContent;
import se.sowl.stitchdomain.study.enumm.ContentType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class StudyContentDetailResponse {
    private Long id;
    private Long studyPostId;
    private String title;
    private String content;
    private ContentType contentType;
    private String writerName;
    private Long userCamInfoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String campusName;
    private String majorName;

    public static StudyContentDetailResponse from(StudyContent studyContent) {
        return StudyContentDetailResponse.builder()
                .id(studyContent.getId())
                .studyPostId(studyContent.getStudyPost().getId())
                .title(studyContent.getTitle())
                .content(studyContent.getContent())
                .contentType(studyContent.getStudyContentType())
                .writerName(studyContent.getUserCamInfo().getUser().getName())
                .userCamInfoId(studyContent.getUserCamInfo().getId())
                .createdAt(studyContent.getCreatedAt())
                .updatedAt(studyContent.getUpdatedAt())
                .campusName(studyContent.getUserCamInfo().getCampus().getName())
                .majorName(studyContent.getUserCamInfo().getMajor() != null ?
                        studyContent.getUserCamInfo().getMajor().getName() : null)
                .build();
    }
}