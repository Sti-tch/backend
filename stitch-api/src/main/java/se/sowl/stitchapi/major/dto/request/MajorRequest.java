package se.sowl.stitchapi.major.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "전공 선택 요청")
public class MajorRequest {
    @Schema(description = "전공 ID", example = "1")
    private Long majorId;
    @Schema(description = "사용자 ID", example = "12345")
    private Long userId;
}
