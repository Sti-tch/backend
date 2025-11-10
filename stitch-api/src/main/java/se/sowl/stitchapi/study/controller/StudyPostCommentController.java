package se.sowl.stitchapi.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.StudyPostCommentRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostCommentResponse;
import se.sowl.stitchapi.study.service.StudyPostCommentService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

import java.util.List;

@RestController
@RequestMapping("/api/study-comments")
@RequiredArgsConstructor
@Tag(name = "StudyPostComment", description = "스터디 게시글 댓글 API")
public class StudyPostCommentController {

    private final StudyPostCommentService studyPostCommentService;

    @Operation(summary = "스터디 게시글 댓글 조회", description = "특정 스터디 게시글에 대한 댓글을 조회합니다.")
    @GetMapping("/studies/{studyPostId}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<StudyPostCommentResponse>> getCommentsByPostId(
            @PathVariable Long studyPostId
    ){
        List<StudyPostCommentResponse> response = studyPostCommentService.getCommentsByPostId(studyPostId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 댓글 작성", description = "새로운 스터디 게시글 댓글을 작성합니다.")
    @PostMapping
    public CommonResponse<StudyPostCommentResponse> createStudyComments(
            @RequestBody StudyPostCommentRequest studyPostCommentRequest,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        StudyPostCommentResponse response = studyPostCommentService.createStudyComments(studyPostCommentRequest, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 댓글 수정", description = "기존 스터디 게시글 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public CommonResponse<StudyPostCommentResponse> updateStudyPostComments(
            @PathVariable Long commentId,
            @RequestBody StudyPostCommentRequest studyPostCommentRequest,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        StudyPostCommentResponse response = studyPostCommentService.updateStudyPostComment(commentId, studyPostCommentRequest, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 댓글 삭제", description = "기존 스터디 게시글 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public CommonResponse<Void> deleteStudyPostComments(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        studyPostCommentService.deleteStudyPostComment(commentId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(null);
    }

    @Operation(summary = "스터디 게시글 댓글 개수 조회", description = "특정 스터디 게시글에 대한 댓글 개수를 조회합니다.")
    @GetMapping("/studies/{studyPostId}/count")
    public CommonResponse<Integer> getCommentCount(
            @PathVariable Long studyPostId
    ){
        int commentCount = studyPostCommentService.getCommentCount(studyPostId);
        return CommonResponse.ok(commentCount);
    }

    @Operation(summary = "내가 작성한 댓글 조회", description = "사용자가 작성한 모든 스터디 게시글 댓글을 조회합니다.")
    @GetMapping("/my-comments")
    public CommonResponse<List<StudyPostCommentResponse>> getMyComments(
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        List<StudyPostCommentResponse> response = studyPostCommentService.getMyComments(currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }
}