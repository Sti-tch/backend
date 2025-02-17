package se.sowl.stitchapi.major.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorRequest {
    private Long majorId;
    private Long userId;
    private boolean isSkipped;
}
