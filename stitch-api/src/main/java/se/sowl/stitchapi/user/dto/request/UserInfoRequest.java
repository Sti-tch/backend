package se.sowl.stitchapi.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "사용자 정보 요청")
public class UserInfoRequest {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "전공 ID", example = "1")
    private Long majorId;

    @Schema(description = "전공명", example = "컴퓨터공학과")
    private String majorName;

    @Schema(description = "이메일", example = "user@google.com")
    private String email;

    @Schema(description = "캠퍼스 인증 여부", example = "true")
    private boolean campusCertified;

    @Schema(description = "캠퍼스 ID", example = "1")
    private Long campusId;

    @Schema(description = "캠퍼스명", example = "서울대학교")
    private String campusName;

    @Schema(description = "이름", example = "김철수")
    private String name;

    @Schema(description = "닉네임", example = "철수김")
    private String nickname;

    @Schema(description = "OAuth 제공자", example = "kakao")
    private String provider;

    @Schema(description = "사용자 캠퍼스 정보 ID", example = "1")
    private Long userCamInfoId;

    @Schema(description = "참여중인 스터디 수", example = "3")
    private int joinedStudyCount;

    @Schema(description = "승인대기 스터디 수", example = "1")
    private int pendingStudyCount;
}