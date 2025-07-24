package se.sowl.stitchapi.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "공통 응답 형식")
public class CommonResponse<T> {

    @Schema(description = "응답 코드", example = "SUCCESS")
    private String code;

    @Schema(description = "응답 메시지", example = "성공")
    private String message;

    @Schema(description = "응답 데이터")
    private T result;

    public CommonResponse(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> CommonResponse<T> ok(T result) {
        return new CommonResponse<>("SUCCESS", "성공", result);
    }

    public static <T> CommonResponse<T> ok() {
        return new CommonResponse<>("SUCCESS", "성공", null);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>("FAIL", message, null);
    }
}