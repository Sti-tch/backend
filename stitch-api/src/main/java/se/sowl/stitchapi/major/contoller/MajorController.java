package se.sowl.stitchapi.major.contoller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.major.dto.request.MajorRequest;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchapi.major.service.MajorService;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "Major", description = "전공 관련 API")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(example = """
                    {
                      "code": "BAD_REQUEST",
                      "message": "잘못된 요청입니다.",
                      "result": null
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
                content = @Content(schema = @Schema(example = """
                    {
                      "code": "INTERNAL_SERVER_ERROR", 
                      "message": "서버 내부 오류가 발생했습니다.",
                      "result": null
                    }
                    """)))
})

public class MajorController {

    private final MajorService majorService;

    @Operation(summary = "전공 목록 조회", description = "모든 전공의 목록을 조회합니다.")
    @GetMapping("/list")
    public CommonResponse<List<MajorListResponse>> getMajorList(){
        List<MajorListResponse> majorList = majorService.getAllMajors();
        return CommonResponse.ok(majorList);
    }

    @Operation(summary = "전공 상세 조회", description = "특정 전공의 상세 정보를 조회합니다.")
    @GetMapping("/detail")
    public CommonResponse<MajorDetailResponse> getMajorDetail(
            @Parameter(description = "전공 ID", required = true)
            @RequestParam("majorId") Long majorId)
    {
        MajorDetailResponse response = majorService.getMajorDetail(majorId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "전공 선택", description = "사용자가 전공을 선택합니다.")
    @PostMapping("/select")
    public CommonResponse<MajorResponse> selectMajor(@RequestBody MajorRequest request){
        MajorResponse response = majorService.selectMajor(request);
        return CommonResponse.ok(response);
    }
}
