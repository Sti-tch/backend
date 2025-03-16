package se.sowl.stitchapi.study.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyPostRequest {
    private String title;
    private String content;
    private StudyStatus status;
}
