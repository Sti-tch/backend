package se.sowl.stitchapi.univcert.storage;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeStorage {

    private final Map<String, VerificationCode> codeStorage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void storeCode(String email, int code) {
        VerificationCode verificationCode = new VerificationCode(code, System.currentTimeMillis());
        codeStorage.put(email, verificationCode);

        // 5분 후 자동 삭제
        scheduler.schedule(() -> codeStorage.remove(email), 5, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, int code) {
        VerificationCode stored = codeStorage.get(email);
        if (stored == null) {
            return false;
        }

        // 5분 만료 체크
        if (System.currentTimeMillis() - stored.getTimestamp() > 300000) {
            codeStorage.remove(email);
            return false;
        }

        return stored.getCode() == code;
    }

    public void removeCode(String email) {
        codeStorage.remove(email);
    }

    public boolean hasValidCode(String email) {
        VerificationCode stored = codeStorage.get(email);
        if (stored == null) {
            return false;
        }

        if (System.currentTimeMillis() - stored.getTimestamp() > 300000) {
            codeStorage.remove(email);
            return false;
        }

        return true;
    }

    private static class VerificationCode {
        private final int code;
        private final long timestamp;

        public VerificationCode(int code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }

        public int getCode() { return code; }
        public long getTimestamp() { return timestamp; }
    }
}