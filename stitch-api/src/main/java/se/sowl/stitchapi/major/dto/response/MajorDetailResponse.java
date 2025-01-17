package se.sowl.stitchapi.major.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se.sowl.stitchdomain.school.domain.Major;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MajorDetailResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static MajorDetailResponse from(Major major) {
        return new MajorDetailResponse(
                major.getId(),
                major.getName(),
                major.getCreatedAt()
        );
    }

}
