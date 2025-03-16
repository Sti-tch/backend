package se.sowl.stitchapi.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyPostResponse {
    private Long id;
    private String title;
    private String content;
    private StudyStatus studyStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StudyPostResponse from(StudyPost studyPost) {
        return new StudyPostResponse(
                studyPost.getId(),
                studyPost.getTitle(),
                studyPost.getContent(),
                studyPost.getStudyStatus(),
                studyPost.getCreatedAt(),
                studyPost.getUpdatedAt()
        );
    }
}
