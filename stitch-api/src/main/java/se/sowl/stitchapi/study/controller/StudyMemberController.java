package se.sowl.stitchapi.study.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.ChangeLeaderRequest;
import se.sowl.stitchapi.study.dto.request.StudyMemberApplyRequest;
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
    public CommonResponse<StudyMemberResponse> rejectStudyMember(
            @RequestParam("studyMemberId") Long studyMemberId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyMemberResponse response = studyMemberService.rejectStudyMember(studyMemberId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @GetMapping("/list")
    public CommonResponse<List<StudyMemberResponse>> getStudyMember(){
        List<StudyMemberResponse> responses = studyMemberService.getStudyMembers();
        return CommonResponse.ok(responses);
    }

    @PostMapping("/leave")
    public CommonResponse<Void> leaveStudyMember(
            @RequestParam("studyMemberId") Long studyMemberId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyMemberService.leaveStudyMember(studyMemberId, userCamInfoId);
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
}
