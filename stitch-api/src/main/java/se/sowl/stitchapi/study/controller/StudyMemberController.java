package se.sowl.stitchapi.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.ChangeLeaderRequest;
import se.sowl.stitchapi.study.dto.request.StudyMemberApplyRequest;
import se.sowl.stitchapi.study.dto.response.MyStudyResponse;
import se.sowl.stitchapi.study.dto.response.StudyMemberResponse;
import se.sowl.stitchapi.study.service.StudyMemberService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

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
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        StudyMemberResponse response = studyMemberService.applyStudyMember(request, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 가입 승인", description = "스터디 가입 신청을 승인합니다. (리더만 가능)")
    @PostMapping("/{studyMemberId}/approve")
    public CommonResponse<StudyMemberResponse> approveStudyMember(
            @PathVariable Long studyMemberId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        StudyMemberResponse response = studyMemberService.approveStudyMember(studyMemberId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 가입 거절", description = "스터디 가입 신청을 거절합니다. (리더만 가능)")
    @PostMapping("/{studyMemberId}/reject")
    public CommonResponse<Void> rejectStudyMember(
            @PathVariable Long studyMemberId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        studyMemberService.rejectStudyMember(studyMemberId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(null);
    }

    @Operation(summary = "스터디 멤버 목록 조회", description = "승인된 스터디 멤버 목록을 조회합니다.")
    @GetMapping("/studies/{studyPostId}")
    public CommonResponse<List<StudyMemberResponse>> getStudyMember(
            @PathVariable Long studyPostId
    ) {
        List<StudyMemberResponse> responses = studyMemberService.getStudyMembers(studyPostId);
        return CommonResponse.ok(responses);
    }

    @Operation(summary = "스터디 탈퇴", description = "스터디에서 탈퇴합니다. (리더는 탈퇴 불가)")
    @DeleteMapping("/studies/{studyPostId}/leave")
    public CommonResponse<Void> leaveStudyMember(
            @PathVariable Long studyPostId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        studyMemberService.leaveStudyMember(studyPostId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(null);
    }

    @Operation(summary = "리더 변경", description = "스터디 리더를 변경합니다. (현재 리더만 가능)")
    @PutMapping("/leader")
    public CommonResponse<StudyMemberResponse> changeLeader(
            @RequestBody ChangeLeaderRequest request,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ) {
        StudyMemberResponse response = studyMemberService.changeLeader(request, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 신청자 목록 조회", description = "내 스터디의 신청자 목록을 조회합니다. (리더만 가능)")
    @GetMapping("/studies/{studyPostId}/applicants")
    public CommonResponse<List<StudyMemberResponse>> getStudyApplicants(
            @PathVariable Long studyPostId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ) {
        List<StudyMemberResponse> responses = studyMemberService.getApplicantsMyStudy(studyPostId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(responses);
    }

    @Operation(summary = "내가 관련된 모든 스터디 목록", description = "내가 관련된 모든 스터디 목록을 조회합니다. (신청, 승인 상태 모두 포함)")
    @GetMapping("/my-studies")
    public CommonResponse<List<MyStudyResponse>> getMyStudies(
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ) {
        List<MyStudyResponse> responses = studyMemberService.getMyJoinedStudies(currentUser.getUserCamInfoId());
        return CommonResponse.ok(responses);
    }

}
