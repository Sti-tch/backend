package se.sowl.stitchapi.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.user.dto.request.EditUserRequest;
import se.sowl.stitchapi.user.dto.request.UserInfoRequest;
import se.sowl.stitchapi.user.service.UserService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

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

    /**
     * 로그아웃 처리 (POST 방식)
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<String> logout(HttpServletRequest request) {
        try {
            // 현재 세션 가져오기
            HttpSession session = request.getSession(false);

            if (session != null) {
                // 세션 무효화
                session.invalidate();
            }

            return CommonResponse.ok("로그아웃이 완료되었습니다.");

        } catch (Exception e) {
            return CommonResponse.fail("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그아웃 처리 (GET 방식 - 브라우저 직접 접근용)
     */
    @GetMapping("/logout")
    public void logoutGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            // 로그인 페이지로 리다이렉트
            response.sendRedirect("/login");

        } catch (Exception e) {
            // 에러 발생 시 메인 페이지로 리다이렉트
            response.sendRedirect("/");
        }
    }
}