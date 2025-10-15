package se.sowl.stitchdomain.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final User user;
    private final OAuth2User oauth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getEmail() {return user.getEmail();}

    // 추가적인 사용자 정보 메서드
    public Long getUserCamInfoId() {
        if (user.getUserCamInfo() != null) {
            return user.getUserCamInfo().getId();
        }
        return null;
    }

    // 추가적인 사용자 정보 메서드(학교 인증 관련)
    public boolean isCampusCertified() {
        return user.isCampusCertified();
    }
}
