package se.sowl.stitchapi.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class UserInfoRequest {
    private Long userId;
    private Long majorId;
    private String email;
    private boolean campusCertified;
    private String name;
    private String nickname;
    private String provider;
}