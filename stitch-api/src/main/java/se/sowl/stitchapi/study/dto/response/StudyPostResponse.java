package se.sowl.stitchapi.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "스터디 게시글 응답")
public class StudyPostResponse {
    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "Java 스터디 모집합니다")
    private String title;

    @Schema(description = "내용", example = "매주 토요일 오후 2시에 스터디를 진행합니다")
    private String content;

    @Schema(description = "스터디 상태", example = "RECRUITING")
    private StudyStatus studyStatus;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
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