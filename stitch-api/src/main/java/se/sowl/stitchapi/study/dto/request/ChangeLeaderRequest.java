package se.sowl.stitchapi.study.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLeaderRequest {
    private Long studyPostId;
    private Long newLeaderId;
}
