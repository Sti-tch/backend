package se.sowl.stitchapi.major.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.school.domain.Major;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MajorResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static MajorResponse from(Major major) {
        return new MajorResponse(
                major.getId(),
                major.getName(),
                major.getCreatedAt()
        );
    }
}