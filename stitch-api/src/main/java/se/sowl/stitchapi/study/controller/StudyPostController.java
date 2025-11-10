package se.sowl.stitchapi.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostListResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostResponse;
import se.sowl.stitchapi.study.service.StudyPostService;
import se.sowl.stitchdomain.user.domain.CustomOAuth2User;

import java.util.List;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
@Tag(name = "StudyPost", description = "스터디 게시글 관련 API")
public class StudyPostController {

    private final StudyPostService studyPostService;

    @Operation(summary = "스터디 게시글 생성")
    @PostMapping
    public CommonResponse<StudyPostResponse> createStudyPost(
            @RequestBody StudyPostRequest studyPostRequest,
            @AuthenticationPrincipal CustomOAuth2User currentUser
            ){
        StudyPostResponse response = studyPostService.createStudyPost(studyPostRequest, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 상세 조회")
    @GetMapping("/{studyPostId}")
    public CommonResponse<StudyPostDetailResponse> getStudyPostDetail(
            @PathVariable Long studyPostId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        StudyPostDetailResponse response = studyPostService.getStudyPostDetail(studyPostId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 수정")
    @PutMapping("/{studyPostId}")
    public CommonResponse<StudyPostResponse> updateStudyPost(
            @PathVariable Long studyPostId,
            @RequestBody StudyPostRequest studyPostRequest,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        StudyPostResponse response = studyPostService.updateStudyPost(studyPostRequest, studyPostId , currentUser.getUserCamInfoId());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "스터디 게시글 삭제")
    @DeleteMapping("/{studyPostId}")
    public CommonResponse<Void> deleteStudyPost(
            @PathVariable Long studyPostId,
            @AuthenticationPrincipal CustomOAuth2User currentUser
    ){
        studyPostService.deleteStudyPost(studyPostId, currentUser.getUserCamInfoId());
        return CommonResponse.ok(null);
    }

    @Operation(summary = "스터디 게시글 목록 조회")
    @GetMapping
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
