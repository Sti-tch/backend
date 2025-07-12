package se.sowl.stitchapi.campus.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.school.domain.Campus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "캠퍼스 목록 응답")
public class CampusListResponse {

    @Schema(description = "캠퍼스 ID", example = "1")
    private Long id;

    @Schema(description = "캠퍼스명", example = "서울대학교")
    private String name;

    @Schema(description = "도메인", example = "sun.ac.kr")
    private String domain;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    public static CampusListResponse from(Campus campus) {
        return new CampusListResponse(
                campus.getId(),
                campus.getName(),
                campus.getDomain(),
                campus.getCreatedAt()
        );
    }
}
