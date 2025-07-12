package se.sowl.stitchapi.campus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.stitchapi.campus.dto.response.CampusListResponse;
import se.sowl.stitchapi.campus.service.CampusService;
import se.sowl.stitchapi.common.CommonResponse;

import java.util.List;

@RestController
@RequestMapping("/api/campus")
@RequiredArgsConstructor
@Tag(name = "Campus", description = "캠퍼스 관련 API")
public class CampusController {

    private final CampusService campusService;

    @Operation(summary = "캠퍼스 목록 조회", description = "모든 캠퍼스의 목록을 조회합니다.")
    @GetMapping("/list")
    public CommonResponse<List<CampusListResponse>> getCampusList(){
        List<CampusListResponse> campusListResponse = campusService.getAllCampuses();
        return CommonResponse.ok(campusListResponse);
    }
}
