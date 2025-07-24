package se.sowl.stitchapi.univcert.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.univcert.service.UnivCertService;
import se.sowl.stitchapi.univcert.dto.EmailVerificationRequest;
import se.sowl.stitchapi.univcert.dto.CodeVerificationRequest;
import se.sowl.stitchapi.user_cam_info.service.UserCamInfoService;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "UnivCert", description = "대학교 인증 관련 API")
public class UnivCertController {

    private final UnivCertService univCertService;
    private final CampusRepository campusRepository;
    private final UserCamInfoService userCamInfoService;
    private final UserRepository userRepository;

    @Operation(summary = "대학교 인증 이메일 발송")
    @PostMapping("/university/verify")
    public ResponseEntity<Map<String, Object>> sendVerificationEmail(
            @RequestBody EmailVerificationRequest request) {

        // 1. 현재 로그인한 사용자 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
        String userEmail = oAuth2User.getEmail();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException());

        // 2. 이미 인증된 사용자인지 확인
        if (user.isCampusCertified()) {
            throw new UserException.UserAlreadyCertifiedException();
        }

        // 3. 이메일 도메인 검증
        String emailDomain = extractDomain(request.getEmail());
        Campus campus = campusRepository.findByName(request.getUnivName())
                .orElseThrow(() -> new UserException.CampusNotFoundException());

        if (!emailDomain.endsWith(campus.getDomain())) {
            throw new UserException.InvalidCampusEmailDomainException();
        }

        // 4. 기존 인증 상태 초기화
        univCertService.clearVerificationStatus(request.getEmail());

        // 5. 인증 이메일 발송
        Map<String, Object> response = univCertService.sendVerificationEmail(
                request.getEmail(),
                request.getUnivName()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "대학교 인증 코드 검증")
    @PostMapping("/university/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(
            @RequestBody CodeVerificationRequest request) {

        // 1. 현재 로그인한 사용자 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
        String userEmail = oAuth2User.getEmail();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException());

        // 2. 인증 코드 검증
        Map<String, Object> response = univCertService.verifyCode(
                request.getEmail(),
                request.getUnivName(),
                request.getCode()
        );

        // 3. 코드 확인이 성공했을 때만 UserCamInfo 생성
        if ((int)response.get("code") == 200) {
            try {
                userCamInfoService.createUserCamInfo(
                        user.getId(),
                        request.getEmail(),
                        request.getUnivName()
                );
                response.put("message", "대학교 인증이 완료되었습니다.");
            } catch (Exception e) {
                // UserCamInfo 생성 실패시 응답 수정
                response.put("code", 500);
                response.put("message", "인증은 성공했으나 계정 설정 중 오류가 발생했습니다.");
                response.put("success", false);
            }
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "대학교 인증 상태 확인")
    @GetMapping("/university/status")
    public ResponseEntity<Map<String, Object>> checkStatus(
            @RequestParam String email) {
        Map<String, Object> response = univCertService.checkVerificationStatus(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "대학교 이름 유효성 검사")
    @GetMapping("/university/check")
    public ResponseEntity<Map<String, Object>> checkUniversity(
            @RequestParam String univName) {
        Map<String, Object> response = univCertService.isValidUniversity(univName);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "대학교 인증 상태 초기화")
    @PostMapping("/university/clear")
    public ResponseEntity<Map<String, Object>> clearVerificationStatus(
            @RequestParam String email) {
        Map<String, Object> response = univCertService.clearVerificationStatus(email);
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일에서 도메인 추출
     */
    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            throw new UserException.InvalidEmailFormatException();
        }
        return email.substring(email.indexOf("@") + 1);
    }
}