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
@Schema(description = "리더 변경 요청")
public class ChangeLeaderRequest {
    @Schema(description = "스터디 게시글 ID", example = "1")
    private Long studyPostId;

    @Schema(description = "새 리더가 될 멤버 ID", example = "5")
    private Long newLeaderId;
}
