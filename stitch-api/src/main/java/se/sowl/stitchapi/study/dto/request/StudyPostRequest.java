package se.sowl.stitchapi.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "스터디 게시글 요청")
public class StudyPostRequest {
    @Schema(description = "제목", example = "Java 스터디 모집합니다")
    private String title;

    @Schema(description = "내용", example = "매주 토요일 오후 2시에 스터디를 진행합니다")
    private String content;

    @Schema(description = "스터디 상태", example = "RECRUITING")
    private StudyStatus status;
}
