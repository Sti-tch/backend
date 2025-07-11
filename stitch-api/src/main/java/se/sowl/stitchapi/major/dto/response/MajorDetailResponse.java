package se.sowl.stitchapi.major.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.sowl.stitchdomain.school.domain.Major;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "전공 상세 정보 응답")
public class MajorDetailResponse {

    @Schema(description = "전공 ID", example = "1")
    private Long id;

    @Schema(description = "전공 이름", example = "컴퓨터공학과")
    private String name;

    @Schema(description = "전공 생성 시간")
    private LocalDateTime createdAt;

    public static MajorDetailResponse from(Major major) {
        return new MajorDetailResponse(
                major.getId(),
                major.getName(),
                major.getCreatedAt()
        );
    }

}
