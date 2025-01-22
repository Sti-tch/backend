package se.sowl.stitchapi.campus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.school.domain.Campus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CampusListResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static CampusListResponse from(Campus campus) {
        return new CampusListResponse(
                campus.getId(),
                campus.getName(),
                campus.getCreatedAt()
        );
    }
}
