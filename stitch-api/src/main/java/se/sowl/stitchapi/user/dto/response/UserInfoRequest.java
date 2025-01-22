package se.sowl.stitchapi.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class UserInfoRequest {
    private Long userId;
    private String email;
    private String name;
    private String nickname;
    private String provider;
}