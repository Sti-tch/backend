package se.sowl.stitchapi.user_cam_info.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchapi.user_cam_info.service.UserCamInfoService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-cam-infos")
public class UserCamInfoController {

    private final UserCamInfoService userCamInfoService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<UserCamInfoResponse> getCamInfoMe(@AuthenticationPrincipal CustomOAuth2User user) {
        UserCamInfoResponse userCamInfoResponse = userCamInfoService.getUserCamInfo(user.getUserId());
        return CommonResponse.ok(userCamInfoResponse);
    }
}
