package se.sowl.stitchapi.major.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.school.domain.Major;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MajorListResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static MajorListResponse from(Major major) {
        return new MajorListResponse(
                major.getId(),
                major.getName(),
                major.getCreatedAt()
        );
    }
}
