package se.sowl.stitchapi.study.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.ChangeLeaderRequest;
import se.sowl.stitchapi.study.dto.request.StudyMemberApplyRequest;
import se.sowl.stitchapi.study.dto.response.MyStudyResponse;
import se.sowl.stitchapi.study.dto.response.StudyMemberResponse;
import se.sowl.stitchapi.study.service.StudyMemberService;

import java.util.List;

@RestController
@RequestMapping("/api/study-members")
@RequiredArgsConstructor
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    @PostMapping("/apply")
    public CommonResponse<StudyMemberResponse> applyStudyMember(
            @RequestBody StudyMemberApplyRequest request,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyMemberResponse response = studyMemberService.applyStudyMember(request, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/approve")
    public CommonResponse<StudyMemberResponse> approveStudyMember(
            @RequestParam("studyMemberId") Long studyMemberId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyMemberResponse response = studyMemberService.approveStudyMember(studyMemberId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/reject")
    public CommonResponse<Void> rejectStudyMember(
            @RequestParam("studyMemberId") Long studyMemberId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyMemberService.rejectStudyMember(studyMemberId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    @GetMapping("/list")
    public CommonResponse<List<StudyMemberResponse>> getStudyMember(@RequestParam("studyPostId") Long studyPostId) {
        List<StudyMemberResponse> responses = studyMemberService.getStudyMembers(studyPostId);
        return CommonResponse.ok(responses);
    }

    @PostMapping("/leave")
    public CommonResponse<Void> leaveStudyMember(
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyMemberService.leaveStudyMember(studyPostId, userCamInfoId);
        return CommonResponse.ok(null);
    }


    @PostMapping("/change-leader")
    public CommonResponse<StudyMemberResponse> changeLeader(
            @RequestBody ChangeLeaderRequest request,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ) {
        StudyMemberResponse response = studyMemberService.changeLeader(request, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @GetMapping("/applicants")
    public CommonResponse<List<StudyMemberResponse>> getStudyApplicants(
            @RequestParam("studyPostId") Long studyPostId,
            @RequestParam("userCamInfoId") Long userCamInfoId
            ) {
        List<StudyMemberResponse> responses = studyMemberService.getApplicantsMyStudy(studyPostId, userCamInfoId);
        return CommonResponse.ok(responses);
    }

    @GetMapping("/my-applications")
    public CommonResponse<List<MyStudyResponse>> getMyApplications(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ) {
        List<MyStudyResponse> responses = studyMemberService.getMyApplications(userCamInfoId);
        return CommonResponse.ok(responses);
    }

    @GetMapping("/my-studies")
    public CommonResponse<List<MyStudyResponse>> getMyStudies(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ) {
        List<MyStudyResponse> responses = studyMemberService.getMyJoinedStudies(userCamInfoId);
        return CommonResponse.ok(responses);
    }

}
