package se.sowl.stitchapi.univcert.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "대학교 인증 코드 검증 요청")
public class CodeVerificationRequest {

    @Schema(description = "인증 이메일", example = "example@sun.ac.kr")
    private String email;

    @Schema(description = "대학교 이름", example = "서울대학교")
    private String univName;

    @Schema(description = "인증 코드", example = "123456")
    private int code;

    @Schema(description = "전공 ID", example = "1")
    private Long majorId;
}