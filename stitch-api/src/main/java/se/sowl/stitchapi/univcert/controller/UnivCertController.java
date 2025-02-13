package se.sowl.stitchapi.univcert.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.exception.CampusException;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.univcert.service.UnivCertService;
import se.sowl.stitchapi.univcert.dto.EmailVerificationRequest;
import se.sowl.stitchapi.univcert.dto.CodeVerificationRequest;
import se.sowl.stitchapi.user_cam_info.service.UserCamInfoService;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UnivCertController {
    private final UnivCertService univCertService;
    private final CampusRepository campusRepository;
    private final UserCamInfoService userCamInfoService;
    private final UserRepository userRepository;

    @PostMapping("/university/verify")
    public ResponseEntity<Map<String, Object>> sendVerificationEmail(
            @RequestBody EmailVerificationRequest request) {

        univCertService.clearVerificationStatus(request.getEmail());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
        String userEmail = oAuth2User.getEmail();  // getName() 대신 getEmail() 사용

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException());

        Map<String, Object> response = univCertService.sendVerificationEmail(
                request.getEmail(),
                request.getUnivName()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/university/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(
            @RequestBody CodeVerificationRequest request) {
        Map<String, Object> response = univCertService.verifyCode(
                request.getEmail(),
                request.getUnivName(),
                request.getCode()
        );

        // 2. 코드 확인이 성공했을 때만 UserCamInfo 생성
        if ((int)response.get("code") == 200) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String userEmail = oAuth2User.getEmail();

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserException.UserNotFoundException());

            userCamInfoService.createUserCamInfo(
                    user.getId(),
                    request.getEmail(),
                    request.getUnivName()
            );
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/university/status")
    public ResponseEntity<Map<String, Object>> checkStatus(
            @RequestParam String email) {
        Map<String, Object> response = univCertService.checkVerificationStatus(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/university/check")
    public ResponseEntity<Map<String, Object>> checkUniversity(
            @RequestParam String univName) {
        Map<String, Object> response = univCertService.isValidUniversity(univName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/university/clear")
    public ResponseEntity<Map<String, Object>> clearVerificationStatus(
            @RequestParam String email) {
        Map<String, Object> response = univCertService.clearVerificationStatus(email);
        return ResponseEntity.ok(response);
    }
}