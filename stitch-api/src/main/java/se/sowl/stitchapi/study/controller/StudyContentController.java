package se.sowl.stitchapi.study.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.StudyContentRequest;
import se.sowl.stitchapi.study.dto.response.StudyContentDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyContentListResponse;
import se.sowl.stitchapi.study.dto.response.StudyContentResponse;
import se.sowl.stitchapi.study.service.StudyContentService;

import java.util.List;

@RestController
@RequestMapping("/api/study-contents")
@RequiredArgsConstructor
public class StudyContentController {

    private final StudyContentService studyContentService;

    @PostMapping("/create")
    public CommonResponse<StudyContentResponse> createStudyContent(
            @RequestBody StudyContentRequest request,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyContentResponse response = studyContentService.createStudyContent(request, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @GetMapping("/list")
    public CommonResponse<List<StudyContentListResponse>> getStudyContentList(
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        List<StudyContentListResponse> responses = studyContentService.getStudyContentsByStudyPost(studyPostId, userCamInfoId);
        return CommonResponse.ok(responses);
    }

    @GetMapping("/detail")
    public CommonResponse<StudyContentDetailResponse> getStudyContentDetail(
            @RequestParam("studyContentId") Long studyContentId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyContentDetailResponse response = studyContentService.getStudyContentDetail(studyContentId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/update")
    public CommonResponse<StudyContentResponse> updateStudyContent(
            @RequestParam("contentId") Long studyContentId,
            @RequestBody StudyContentRequest request,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyContentResponse response = studyContentService.updateStudyContent(studyContentId, request, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/delete")
    public CommonResponse<Void> deleteStudyContent(
            @RequestParam("studyContentId") Long studyContentId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyContentService.deleteStudyContent(studyContentId, userCamInfoId);
        return CommonResponse.ok();
    }
}
