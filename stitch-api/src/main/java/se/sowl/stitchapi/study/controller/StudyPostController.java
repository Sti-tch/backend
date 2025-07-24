package se.sowl.stitchapi.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostListResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostResponse;
import se.sowl.stitchapi.study.service.StudyPostService;

import java.util.List;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
@Tag(name = "StudyPost", description = "스터디 게시글 관련 API")
public class StudyPostController {

    private final StudyPostService studyPostService;

    @Operation(summary = "스터디 게시글 생성")
    @PostMapping("/create")
    public CommonResponse<StudyPostResponse> createStudyPost(
            @RequestBody StudyPostRequest studyPostRequest,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostResponse response = studyPostService.createStudyPost(studyPostRequest, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 상세 조회")
    @GetMapping("/detail")
    public CommonResponse<StudyPostDetailResponse> getStudyPostDetail(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostDetailResponse response = studyPostService.getStudyPostDetail(studyPostId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 수정")
    @PostMapping("/update")
    public CommonResponse<StudyPostResponse> updateStudyPost(
            @RequestBody StudyPostRequest studyPostRequest,
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        StudyPostResponse response = studyPostService.updateStudyPost(studyPostRequest, studyPostId ,userCamInfoId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 삭제")
    @PostMapping("/delete")
    public CommonResponse<Void> deleteStudyPost(
            @Parameter(description = "스터디 게시글 ID", required = true, example = "1")
            @RequestParam("studyPostId") Long studyPostId,
            @Parameter(description = "학교 인증자 ID", required = true, example = "1")
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        studyPostService.deleteStudyPost(studyPostId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    @Operation(summary = "스터디 게시글 목록 조회")
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<StudyPostListResponse>> getStudyPostList(){
        List<StudyPostListResponse> postLists = studyPostService.getStudyPostLists();
        return CommonResponse.ok(postLists);
    }

    @Operation(summary = "스터디 게시글 검색")
    @GetMapping("/search")
    public CommonResponse<List<StudyPostListResponse>> searchStudyPosts(
            @Parameter(description = "검색 키워드", required = true, example = "Java")
            @RequestParam("keyword") String keyword
    ){
        List<StudyPostListResponse> searchResult = studyPostService.searchPosts(keyword);
        return CommonResponse.ok(searchResult);
    }
}
