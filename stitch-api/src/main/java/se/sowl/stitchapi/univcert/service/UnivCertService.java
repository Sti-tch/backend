package se.sowl.stitchapi.univcert.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import se.sowl.stitchapi.univcert.storage.VerificationCodeStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnivCertService {

    private final JavaMailSender mailSender;
    private final VerificationCodeStorage codeStorage;
    private final UniversityValidationService universityValidationService;
    private final Random random = new Random();

    public Map<String, Object> sendVerificationEmail(String email, String univName) {
        try {
            // 대학교 이름 검증
            if (!universityValidationService.isValidUniversity(univName)) {
                return createErrorResponse(400, "유효하지 않은 대학교입니다.");
            }

            // 6자리 인증 코드 생성
            int verificationCode = 100000 + random.nextInt(900000);

            // 이메일 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[Stitch] 대학교 이메일 인증");
            message.setText(createEmailContent(univName, verificationCode));
            message.setFrom("kcw130502@gmail.com");

            mailSender.send(message);

            // 인증 코드 저장
            codeStorage.storeCode(email, verificationCode);

            log.info("Verification email sent to: {}", email);
            return createSuccessResponse("인증 메일이 발송되었습니다.");

        } catch (Exception e) {
            log.error("Error sending verification email: ", e);
            return createErrorResponse(500, "메일 인증 발송에 실패했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> verifyCode(String email, String univName, int code) {
        try {
            if (codeStorage.verifyCode(email, code)) {
                // 인증 성공 시 코드 삭제
                codeStorage.removeCode(email);
                return createSuccessResponse("인증이 완료되었습니다.");
            } else {
                return createErrorResponse(400, "인증 코드가 올바르지 않거나 만료되었습니다.");
            }
        } catch (Exception e) {
            log.error("Error verifying code: ", e);
            return createErrorResponse(500, "인증 코드 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> checkVerificationStatus(String email) {
        try {
            boolean hasValidCode = codeStorage.hasValidCode(email);
            Map<String, Object> response = createSuccessResponse("인증 상태 확인 완료");
            response.put("verified", hasValidCode);
            return response;
        } catch (Exception e) {
            log.error("Error checking verification status: ", e);
            return createErrorResponse(500, "인증 상태 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> isValidUniversity(String univName) {
        try {
            boolean isValid = universityValidationService.isValidUniversity(univName);
            if (isValid) {
                return createSuccessResponse("유효한 대학교입니다.");
            } else {
                return createErrorResponse(400, "유효하지 않은 대학교입니다.");
            }
        } catch (Exception e) {
            log.error("Error checking university: ", e);
            return createErrorResponse(500, "대학교 이름 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> clearVerificationStatus(String email) {
        try {
            codeStorage.removeCode(email);
            return createSuccessResponse("인증 상태가 초기화되었습니다.");
        } catch (Exception e) {
            log.error("Error clearing verification status: ", e);
            return createErrorResponse(500, "인증 상태 초기화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String createEmailContent(String univName, int code) {
        return String.format(
                """
                안녕하세요, Stitch입니다.
                
                %s 이메일 인증을 위한 인증번호입니다.
                
                인증번호: %d
                
                위 인증번호를 입력하여 이메일 인증을 완료해주세요.
                인증번호는 5분간 유효합니다.
                
                감사합니다.
                Stitch 팀
                """,
                univName, code
        );
    }

    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("success", true);
        return response;
    }

    private Map<String, Object> createErrorResponse(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("success", false);
        return response;
    }
}