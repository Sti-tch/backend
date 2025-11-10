package se.sowl.stitchapi.major.contoller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchapi.major.service.MajorService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "Major", description = "전공 관련 API")
public class MajorController {

    private final MajorService majorService;

    @Operation(summary = "전공 목록 조회")
    @GetMapping
    public CommonResponse<List<MajorListResponse>> getMajorList() {
        List<MajorListResponse> majorList = majorService.getAllMajors();
        return CommonResponse.ok(majorList);
    }

    @Operation(summary = "전공 상세 조회")
    @GetMapping("/{majorId}")
    public CommonResponse<MajorDetailResponse> getMajorDetail(
            @PathVariable Long majorId
    ) {
        MajorDetailResponse response = majorService.getMajorDetail(majorId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "전공 선택")
    @PutMapping("/{majorId}")
    public CommonResponse<MajorResponse> selectMajor(
            @PathVariable Long majorId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ) {
        MajorResponse response = majorService.selectMajor(
                majorId,
                currentUser.getUserId()
        );
        return CommonResponse.ok(response);
    }
}