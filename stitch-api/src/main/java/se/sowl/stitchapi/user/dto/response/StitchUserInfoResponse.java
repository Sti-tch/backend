package se.sowl.stitchapi.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se.sowl.stitchdomain.user.domain.User;

@Getter
@AllArgsConstructor
public class StitchUserInfoResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String provider;
    private boolean isCampusCertified;

    public static StitchUserInfoResponse from(User userInfo) {
        return new StitchUserInfoResponse(
                userInfo.getId(),
                userInfo.getEmail(),
                userInfo.getName(),
                userInfo.getNickname(),
                userInfo.getProvider(),
                userInfo.isCampusCertified()
        );
    }
}
