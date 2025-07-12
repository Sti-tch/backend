package se.sowl.stitchapi.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.StudyPostCommentRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostCommentResponse;
import se.sowl.stitchapi.study.service.StudyPostCommentService;

import java.util.List;

@RestController
@RequestMapping("/api/studyComments")
@RequiredArgsConstructor
@Tag(name = "StudyPostComment", description = "스터디 게시글 댓글 API")
public class StudyPostCommentController {

    private final StudyPostCommentService studyPostCommentService;

    @Operation(summary = "스터디 게시글 댓글 조회", description = "특정 스터디 게시글에 대한 댓글을 조회합니다.")
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<StudyPostCommentResponse>> getCommentsByPostId(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId
    ){
        List<StudyPostCommentResponse> response = studyPostCommentService.getCommentsByPostId(studyPostId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 댓글 작성", description = "새로운 스터디 게시글 댓글을 작성합니다.")
    @PostMapping("/create")
    public CommonResponse<StudyPostCommentResponse> createStudyComments(
            @RequestBody StudyPostCommentRequest studyPostCommentRequest,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
            ){
        StudyPostCommentResponse response = studyPostCommentService.createStudyComments(studyPostCommentRequest, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 댓글 수정", description = "기존 스터디 게시글 댓글을 수정합니다.")
    @PostMapping("/update")
    public CommonResponse<StudyPostCommentResponse> updateStudyPostComments(
            @RequestBody StudyPostCommentRequest studyPostCommentRequest,
            @Parameter(description = "댓글 ID", required = true, example = "1")
            @RequestParam("commentId") Long commentId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostCommentResponse response = studyPostCommentService.updateStudyPostComment(commentId, studyPostCommentRequest, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 댓글 삭제", description = "기존 스터디 게시글 댓글을 삭제합니다.")
    @PostMapping("/delete")
    public CommonResponse<Void> deleteStudyPostComments(
            @Parameter(description = "댓글 ID", required = true, example = "1")
            @RequestParam("commentId") Long commentId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyPostCommentService.deleteStudyPostComment(commentId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    @Operation(summary = "스터디 게시글 댓글 개수 조회", description = "특정 스터디 게시글에 대한 댓글 개수를 조회합니다.")
    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<Integer> getCommentCount(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId
    ){
        int commentCount = studyPostCommentService.getCommentCount(studyPostId);
        return CommonResponse.ok(commentCount);
    }

    @Operation(summary = "내가 작성한 댓글 조회", description = "사용자가 작성한 모든 스터디 게시글 댓글을 조회합니다.")
    @GetMapping("/myComments")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<StudyPostCommentResponse>> getMyComments(
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        List<StudyPostCommentResponse> response = studyPostCommentService.getMyComments(userCamInfoId);
        return CommonResponse.ok(response);
    }
}
