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
public class StudyContentListResponse {
    private Long id;
    private String title;
    private ContentType contentType;
    private String writerName;
    private LocalDateTime createdAt;
    private String campusName;
    private String majorName;

    public static StudyContentListResponse from(StudyContent studyContent) {
        return StudyContentListResponse.builder()
                .id(studyContent.getId())
                .title(studyContent.getTitle())
                .contentType(studyContent.getStudyContentType())
                .writerName(studyContent.getUserCamInfo().getUser().getName())
                .createdAt(studyContent.getCreatedAt())
                .campusName(studyContent.getUserCamInfo().getCampus().getName())
                .majorName(studyContent.getUserCamInfo().getMajor() != null ?
                        studyContent.getUserCamInfo().getMajor().getName() : null)
                .build();
    }
}