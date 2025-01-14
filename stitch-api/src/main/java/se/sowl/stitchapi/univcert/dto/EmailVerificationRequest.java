package se.sowl.stitchapi.univcert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {
    private String email;
    private String univName;
    private Long majorId;
}
