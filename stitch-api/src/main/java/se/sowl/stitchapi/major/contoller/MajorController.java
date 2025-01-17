package se.sowl.stitchapi.major.contoller;


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
public class MajorController {

    private final MajorService majorService;

    @GetMapping("/list")
    public CommonResponse<List<MajorListResponse>> getMajorList(){
        List<MajorListResponse> majorList = majorService.getAllMajors();
        return CommonResponse.ok(majorList);
    }

    @GetMapping("/detail")
    public CommonResponse<MajorDetailResponse> getMajorDetail(@RequestParam("majorId") Long majorId){
        MajorDetailResponse response = majorService.getMajorDetail(majorId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/create")
    public CommonResponse<MajorResponse> createMajor(@RequestBody MajorRequest majorRequest){
        MajorResponse response = majorService.createMajor(majorRequest.getName());
        return CommonResponse.ok(response);
    }
}
