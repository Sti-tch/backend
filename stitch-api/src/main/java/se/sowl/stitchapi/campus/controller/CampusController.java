package se.sowl.stitchapi.campus.controller;

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
public class CampusController {

    private final CampusService campusService;

    @GetMapping("/list")
    public CommonResponse<List<CampusListResponse>> getCampusList(){
        List<CampusListResponse> campusListResponse = campusService.getAllCampuses();
        return CommonResponse.ok(campusListResponse);
    }
}
