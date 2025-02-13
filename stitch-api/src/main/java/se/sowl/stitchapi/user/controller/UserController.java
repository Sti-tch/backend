package se.sowl.stitchapi.user.controller;

import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.user.dto.request.EditUserRequest;
import se.sowl.stitchapi.user.dto.request.UserInfoRequest;
import se.sowl.stitchapi.user.dto.response.StitchUserInfoResponse;
import se.sowl.stitchapi.user.service.UserService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<UserInfoRequest> getMe(@AuthenticationPrincipal CustomOAuth2User user) {
        UserInfoRequest userInfo = userService.getUserInfo(user.getUserId());
        return CommonResponse.ok(userInfo);
    }

    @PutMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<Void> editUser(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody EditUserRequest request) {
        userService.editUser(user.getUserId(), request);
        return CommonResponse.ok();
    }
}
