package se.sowl.stitchapi.study.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostResponse;
import se.sowl.stitchapi.study.service.StudyPostService;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyPostController {

    private final StudyPostService studyPostService;

    @PostMapping("/create")
    public CommonResponse<StudyPostResponse> createStudyPost(
            @RequestBody StudyPostRequest studyPostRequest,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostResponse response = studyPostService.createStudyPost(studyPostRequest, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @GetMapping("/detail")
    public CommonResponse<StudyPostDetailResponse> getStudyPostDetail(
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostDetailResponse response = studyPostService.getStudyPostDetail(studyPostId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PutMapping("/update")
    public CommonResponse<StudyPostResponse> updateStudyPost(
            @RequestBody StudyPostRequest studyPostRequest,
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostResponse response = studyPostService.updateStudyPost(studyPostRequest, studyPostId ,userCamInfoId);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/delete")
    public CommonResponse<StudyPostResponse> deleteStudyPost(
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostResponse response = studyPostService.deleteStudyPost(studyPostId, userCamInfoId);
        return CommonResponse.ok(response);
    }

}
