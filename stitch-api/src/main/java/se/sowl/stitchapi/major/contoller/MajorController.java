package se.sowl.stitchapi.major.contoller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
public class MajorController {

    private final MajorService majorService;

    @Operation(summary = "전공 목록 조회")
    @GetMapping("/list")
    public CommonResponse<List<MajorListResponse>> getMajorList() {
        List<MajorListResponse> majorList = majorService.getAllMajors();
        return CommonResponse.ok(majorList);
    }

    @Operation(summary = "전공 상세 조회")
    @GetMapping("/detail")
    public CommonResponse<MajorDetailResponse> getMajorDetail(
            @Parameter(description = "전공 ID", required = true)
            @RequestParam("majorId") Long majorId) {
        MajorDetailResponse response = majorService.getMajorDetail(majorId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "전공 선택")
    @PostMapping("/select")
    public CommonResponse<MajorResponse> selectMajor(@RequestBody MajorRequest request) {
        MajorResponse response = majorService.selectMajor(request);
        return CommonResponse.ok(response);
    }
}