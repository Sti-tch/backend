package se.sowl.stitchapi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.user.dto.request.UserInfoRequest;
import se.sowl.stitchapi.user.service.UserService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<UserInfoRequest> getMe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User user) {
        UserInfoRequest userInfo = userService.getUserInfo(user.getUserId());
        return CommonResponse.ok(userInfo);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public CommonResponse<String> logout(HttpServletRequest request) {
        try {
            request.logout();
            return CommonResponse.ok("로그아웃이 완료되었습니다.");
        } catch (Exception e) {
            return CommonResponse.fail("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "로그아웃 (리다이렉트)")
    @GetMapping("/logout")
    public void logoutGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            request.logout();
            response.sendRedirect("http://localhost:3000");
        } catch (Exception e) {
            response.sendRedirect("http://localhost:3000");
        }
    }
}