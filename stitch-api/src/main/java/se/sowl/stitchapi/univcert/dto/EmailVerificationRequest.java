package se.sowl.stitchapi.univcert.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "인증 이메일 발송 요청")
public class EmailVerificationRequest {
    private String email;
    private String univName;
}
