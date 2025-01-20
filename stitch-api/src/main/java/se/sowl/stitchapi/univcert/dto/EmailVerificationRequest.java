package se.sowl.stitchapi.univcert.dto;

import com.mysql.cj.log.Log;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {
    private Long userId;
    private String email;
    private String univName;
}
