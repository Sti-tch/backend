package se.sowl.stitchapi.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "StudyMember", description = "스터디 멤버 관리 API")
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    @Operation(summary = "스터디 가입 신청", description = "스터디에 가입 신청을 합니다.")
    @PostMapping("/apply")
    public CommonResponse<StudyMemberResponse> applyStudyMember(
            @RequestBody StudyMemberApplyRequest request,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyMemberResponse response = studyMemberService.applyStudyMember(request, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 가입 승인", description = "스터디 가입 신청을 승인합니다. (리더만 가능)")
    @PostMapping("/approve")
    public CommonResponse<StudyMemberResponse> approveStudyMember(
            @Parameter(description = "스터디 멤버 ID", required = true, example = "1")
            @RequestParam("studyMemberId") Long studyMemberId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyMemberResponse response = studyMemberService.approveStudyMember(studyMemberId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 가입 거절", description = "스터디 가입 신청을 거절합니다. (리더만 가능)")
    @PostMapping("/reject")
    public CommonResponse<Void> rejectStudyMember(
            @Parameter(description = "스터디 멤버 ID", required = true, example = "1")
            @RequestParam("studyMemberId") Long studyMemberId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyMemberService.rejectStudyMember(studyMemberId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    @Operation(summary = "스터디 멤버 목록 조회", description = "승인된 스터디 멤버 목록을 조회합니다.")
    @GetMapping("/list")
    public CommonResponse<List<StudyMemberResponse>> getStudyMember(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId) {
        List<StudyMemberResponse> responses = studyMemberService.getStudyMembers(studyPostId);
        return CommonResponse.ok(responses);
    }

    @Operation(summary = "스터디 탈퇴", description = "스터디에서 탈퇴합니다. (리더는 탈퇴 불가)")
    @PostMapping("/leave")
    public CommonResponse<Void> leaveStudyMember(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyMemberService.leaveStudyMember(studyPostId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    @Operation(summary = "리더 변경", description = "스터디 리더를 변경합니다. (현재 리더만 가능)")
    @PostMapping("/change-leader")
    public CommonResponse<StudyMemberResponse> changeLeader(
            @RequestBody ChangeLeaderRequest request,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ) {
        StudyMemberResponse response = studyMemberService.changeLeader(request, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 신청자 목록 조회", description = "내 스터디의 신청자 목록을 조회합니다. (리더만 가능)")
    @GetMapping("/applicants")
    public CommonResponse<List<StudyMemberResponse>> getStudyApplicants(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ) {
        List<StudyMemberResponse> responses = studyMemberService.getApplicantsMyStudy(studyPostId, userCamInfoId);
        return CommonResponse.ok(responses);
    }

    @Operation(summary = "내가 관련된 모든 스터디 목록", description = "내가 관련된 모든 스터디 목록을 조회합니다. (신청, 승인 상태 모두 포함)")
    @GetMapping("/my-studies")
    public CommonResponse<List<MyStudyResponse>> getMyStudies(
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ) {
        List<MyStudyResponse> responses = studyMemberService.getMyJoinedStudies(userCamInfoId);
        return CommonResponse.ok(responses);
    }

}
