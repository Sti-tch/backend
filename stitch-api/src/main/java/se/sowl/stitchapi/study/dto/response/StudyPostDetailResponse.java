package se.sowl.stitchapi.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class StudyPostDetailResponse {
    private Long id;
    private String title;
    private String content;
    private StudyStatus studyStatus;
    private UserCamInfoResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StudyPostDetailResponse from(StudyPost studyPost) {
        return StudyPostDetailResponse.builder()
                .id(studyPost.getId())
                .title(studyPost.getTitle())
                .content(studyPost.getContent())
                .studyStatus(studyPost.getStudyStatus())
                .author(UserCamInfoResponse.from(studyPost.getUserCamInfo()))
                .createdAt(studyPost.getCreatedAt())
                .updatedAt(studyPost.getUpdatedAt())
                .build();
    }
}
