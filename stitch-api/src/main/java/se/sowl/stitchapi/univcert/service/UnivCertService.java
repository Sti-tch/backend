package se.sowl.stitchapi.univcert.service;

import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;



@Slf4j
@Service
public class UnivCertService {

    private final RestTemplate restTemplate;
    private final String univCertApiKey;

    // 이 생성자 추가
    public UnivCertService(RestTemplate restTemplate, @Qualifier("univCertApiKey") String univCertApiKey) {
        this.restTemplate = restTemplate;
        this.univCertApiKey = univCertApiKey;
    }

    public Map<String, Object> sendVerificationEmail(String email, String univName) {
        try {
            Map<String, Object> response = UnivCert.certify(univCertApiKey, email, univName, false);
            if ((int)response.get("code") != 200) {
                throw new RuntimeException("메일 인증 발송에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("메일 인증 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> verifyCode(String email, String univName, int code) {
        try {
            Map<String, Object> response = UnivCert.certifyCode(univCertApiKey, email, univName, code);
            if ((int)response.get("code") != 200) {
                throw new RuntimeException("인증 코드가 올바르지 않습니다.");
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("인증 코드 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> checkVerificationStatus(String email) {
        try {
            // TODO: Implement UnivCert.status method
            Map<String, Object> response = UnivCert.status(univCertApiKey, email);
            if ((int)response.get("code") != 200) {
                throw new RuntimeException("인증 상태 확인에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("인증 상태 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> isValidUniversity(String univName) {
        try {
            Map<String, Object> response = UnivCert.check(univName);
            if ((int)response.get("code") != 200) {
                throw new RuntimeException("유효하지 않은 대학교입니다.");
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("대학교 이름 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, Object> clearVerificationStatus(String email) {
        try {
            // API 요청 데이터 준비
            String url = "https://univcert.com/api/v1/clear";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = Map.of("key", univCertApiKey);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error clearing verification status: ", e);
            throw new RuntimeException("인증 상태 초기화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}