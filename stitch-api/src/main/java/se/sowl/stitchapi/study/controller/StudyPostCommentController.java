package se.sowl.stitchapi.study.controller;

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
public class StudyPostCommentController {

    private final StudyPostCommentService studyPostCommentService;

    @Transactional
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<StudyPostCommentResponse>> getCommentsByPostId(
            @RequestParam("studyPostId") Long studyPostId
    ){
        List<StudyPostCommentResponse> response = studyPostCommentService.getCommentsByPostId(studyPostId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/create")
    public CommonResponse<StudyPostCommentResponse> createStudyComments(
            @RequestBody StudyPostCommentRequest studyPostCommentRequest,
            @RequestParam("userCamInfoId") Long userCamInfoId
            ){
        StudyPostCommentResponse response = studyPostCommentService.createStudyComments(studyPostCommentRequest, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/update")
    public CommonResponse<StudyPostCommentResponse> updateStudyPostComments(
            @RequestBody StudyPostCommentRequest studyPostCommentRequest,
            @RequestParam("commentId") Long commentId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostCommentResponse response = studyPostCommentService.updateStudyPostComment(commentId, studyPostCommentRequest, userCamInfoId);
        return CommonResponse.ok(response);
    }
    @DeleteMapping("/delete")
    public CommonResponse<Void> deleteStudyPostComments(
            @RequestParam("commentId") Long commentId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyPostCommentService.deleteStudyPostComment(commentId, userCamInfoId);
        return CommonResponse.ok(null);
    }
}
