package se.sowl.stitchapi.univcert.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.exception.CampusException;
import se.sowl.stitchapi.univcert.service.UnivCertService;
import se.sowl.stitchapi.univcert.dto.EmailVerificationRequest;
import se.sowl.stitchapi.univcert.dto.CodeVerificationRequest;
import se.sowl.stitchapi.user_cam_info.service.UserCamInfoService;
import se.sowl.stitchdomain.school.repository.CampusRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UnivCertController {
    private final UnivCertService univCertService;
    private final CampusRepository campusRepository;
    private final UserCamInfoService userCamInfoService;

    @PostMapping("/university/verify")
    public ResponseEntity<Map<String, Object>> sendVerificationEmail(
            @RequestBody EmailVerificationRequest request) {
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

        if (response.get("status").equals("success")) {
            userCamInfoService.createUserCamInfo(
                    request.getEmail(),
                    request.getUnivName(),
                    request.getMajorId()
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
}