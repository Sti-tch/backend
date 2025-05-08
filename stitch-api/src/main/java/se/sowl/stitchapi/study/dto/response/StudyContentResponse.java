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
public class StudyContentResponse {
    private Long id;
    private Long studyPostId;
    private String title;
    private ContentType contentType;
    private LocalDateTime createdAt;
    private String writerName;
    private Long userCamInfoId;
    private String campusName;
    private String majorName;

    public static StudyContentResponse from(StudyContent studyContent) {
        return StudyContentResponse.builder()
                .id(studyContent.getId())
                .studyPostId(studyContent.getStudyPost().getId())
                .title(studyContent.getTitle())
                .contentType(studyContent.getStudyContentType())
                .createdAt(studyContent.getCreatedAt())
                .writerName(studyContent.getUserCamInfo().getUser().getName())
                .userCamInfoId(studyContent.getUserCamInfo().getId())
                .campusName(studyContent.getUserCamInfo().getCampus().getName())
                .majorName(studyContent.getUserCamInfo().getMajor() != null ?
                        studyContent.getUserCamInfo().getMajor().getName() : null)
                .build();
    }
}