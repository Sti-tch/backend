package se.sowl.stitchapi.major.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.school.domain.Major;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "전공 선택 응답")
public class MajorResponse {

    @Schema(description = "전공 ID", example = "1")
    private Long id;

    @Schema(description = "전공명", example = "컴퓨터공학과")
    private String name;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    public static MajorResponse from(Major major) {
        return new MajorResponse(
                major.getId(),
                major.getName(),
                major.getCreatedAt()
        );
    }
}