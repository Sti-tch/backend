package se.sowl.stitchapi.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchdomain.study.domain.StudyPostComment;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class StudyPostCommentResponse {
    private Long id;
    private String content;
    private UserCamInfoResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StudyPostCommentResponse from(StudyPostComment studyPostComment) {
        return StudyPostCommentResponse.builder()
                .id(studyPostComment.getId())
                .content(studyPostComment.getContent())
                .author(UserCamInfoResponse.from(studyPostComment.getUserCamInfo()))
                .createdAt(studyPostComment.getCreatedAt())
                .updatedAt(studyPostComment.getUpdatedAt())
                .build();
    }
}
