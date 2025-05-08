package se.sowl.stitchapi.study.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.study.enumm.ContentType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyContentRequest {

    private Long studyPostId;
    private String title;
    private String content;
    private ContentType contentType;

}