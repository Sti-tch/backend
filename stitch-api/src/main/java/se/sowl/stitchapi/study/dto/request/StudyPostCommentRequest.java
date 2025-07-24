package se.sowl.stitchapi.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "스터디 게시글 댓글 요청")
public class StudyPostCommentRequest {
    @Schema(description = "댓글 내용", example = "저도 참여하고 싶습니다!")
    private String content;

    @Schema(description = "스터디 게시글 ID", example = "1")
    private Long studyPostId;
}