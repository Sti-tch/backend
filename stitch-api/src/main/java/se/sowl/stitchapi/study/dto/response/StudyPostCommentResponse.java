package se.sowl.stitchapi.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchdomain.study.domain.StudyPostComment;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "스터디 게시글 댓글 응답")
public class StudyPostCommentResponse {
    @Schema(description = "댓글 ID", example = "1")
    private Long id;

    @Schema(description = "댓글 내용", example = "저도 참여하고 싶습니다!")
    private String content;

    @Schema(description = "작성자 정보")
    private UserCamInfoResponse author;

    @Schema(description = "학교 인증자 ID", example = "1")
    private Long userCamInfoId;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static StudyPostCommentResponse from(StudyPostComment studyPostComment) {
        return StudyPostCommentResponse.builder()
                .id(studyPostComment.getId())
                .content(studyPostComment.getContent())
                .author(UserCamInfoResponse.from(studyPostComment.getUserCamInfo()))
                .userCamInfoId(studyPostComment.getUserCamInfo().getId())
                .createdAt(studyPostComment.getCreatedAt())
                .updatedAt(studyPostComment.getUpdatedAt())
                .build();
    }
}