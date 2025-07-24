package se.sowl.stitchapi.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "스터디 가입 신청 요청")
public class StudyMemberApplyRequest {
    @Schema(description = "스터디 게시글 ID", example = "1")
    private Long studyPostId;

    @Schema(description = "신청 메시지", example = "열정적으로 참여하겠습니다!")
    private String applyMessage;
}
