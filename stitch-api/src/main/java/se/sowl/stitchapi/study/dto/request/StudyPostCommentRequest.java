package se.sowl.stitchapi.study.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyPostCommentRequest {
    private String content;
    private Long studyPostId;
}
