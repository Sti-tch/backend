package se.sowl.stitchapi.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.study.domain.StudyPost;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyPostListResponse {
    private Long id;
    private String title;
    private String content;
    private String studyStatus;
    private String createdAt;
    private String updatedAt;
    private Long userCamInfoId;

    public static StudyPostListResponse from(StudyPost studyPost) {
        return new StudyPostListResponse(
                studyPost.getId(),
                studyPost.getTitle(),
                studyPost.getContent(),
                studyPost.getStudyStatus().name(),
                studyPost.getCreatedAt().toString(),
                studyPost.getUpdatedAt().toString(),
                studyPost.getUserCamInfo().getId()
        );
    }

}
