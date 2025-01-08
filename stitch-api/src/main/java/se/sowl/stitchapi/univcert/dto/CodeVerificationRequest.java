package se.sowl.stitchapi.univcert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodeVerificationRequest {
    private String email;
    private String univName;
    private int code;
}