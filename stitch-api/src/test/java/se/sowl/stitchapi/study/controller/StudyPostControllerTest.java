package se.sowl.stitchapi.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostResponse;
import se.sowl.stitchapi.study.service.StudyPostService;
import se.sowl.stitchdomain.study.enumm.StudyStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyPostController.class)
class StudyPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudyPostService studyPostService;


    @Nested
    @DisplayName("스터디 게시글 생성")
    class CreateStudyPost{

        @Test
        @DisplayName("POST /api/studies/create")
        @WithMockUser
        void createStudyPost() throws Exception{
            //given
            Long userCamInfoId = 1L;
            StudyPostRequest studyPostRequest = new StudyPostRequest("title", "content", StudyStatus.RECRUITING);
            StudyPostResponse studyPostResponse = new StudyPostResponse(1L, "title", "content", StudyStatus.RECRUITING,null, null);

            //when
            when(studyPostService.createStudyPost(any(StudyPostRequest.class), any(Long.class)))
                    .thenReturn(studyPostResponse);
            //then
            mockMvc.perform(post("/api/studies/create")
                            .with(csrf())
                            .param("userCamInfoId", userCamInfoId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(studyPostRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.id").value(1L))
                    .andExpect(jsonPath("$.result.title").value("title"))
                    .andExpect(jsonPath("$.result.content").value("content"))
                    .andExpect(jsonPath("$.result.studyStatus").value("RECRUITING"))
                    .andDo(print());


        }
    }

    @Nested
    @DisplayName("스터디 게시글 상세 조회")
    class GetStudyPostDetail{

        @Test
        @DisplayName("GET /api/studies/detail")
        @WithMockUser
        void getStudyPostDetail() throws Exception{
            //given
            Long studyPostId = 1L;
            Long userCamInfoId = 1L;
            StudyPostDetailResponse studyPostDetailResponse = new StudyPostDetailResponse(1L, "title", "content", StudyStatus.RECRUITING, null, null,null);

            //when
            when(studyPostService.getStudyPostDetail(eq(studyPostId), eq(userCamInfoId)))
                    .thenReturn(studyPostDetailResponse);

            //then
            mockMvc.perform(get("/api/studies/detail")
                            .param("studyPostId", studyPostId.toString())
                            .param("userCamInfoId", userCamInfoId.toString())
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.id").value(1L))
                    .andExpect(jsonPath("$.result.title").value("title"))
                    .andExpect(jsonPath("$.result.content").value("content"))
                    .andExpect(jsonPath("$.result.studyStatus").value("RECRUITING"))
                    .andDo(print());
        }

        @Test
        @DisplayName("GET /api/studies/detail - 존재하지 않는 게시글 조회 실패")
        @WithMockUser
        void getStudyPostDetailNotFound() throws Exception {
            // given
            Long studyPostId = 9999L;
            Long userCamInfoId = 1L;

            // when
            when(studyPostService.getStudyPostDetail(studyPostId, userCamInfoId))
                    .thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));

            // then
            mockMvc.perform(get("/api/studies/detail")
                            .with(csrf())
                            .param("studyPostId", "9999")
                            .param("userCamInfoId","1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andDo(print());
        }

        @Test
        @DisplayName("GET /api/studies/detail - 접근 권한 없음 실패")
        @WithMockUser
        void getStudyPostDetailUnauthorized() throws Exception {
            // given
            Long studyPostId = 1L;
            Long userCamInfoId = 2L;

            // when
            when(studyPostService.getStudyPostDetail(studyPostId, userCamInfoId))
                    .thenThrow(new IllegalArgumentException("접근 권한이 없습니다."));

            // then
            mockMvc.perform(get("/api/studies/detail")
                            .with(csrf())
                            .param("studyPostId", "1")
                            .param("userCamInfoId", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("스터디 게시글 수정")
    class UpdateStudyPost {

        @Test
        @DisplayName("PUT /api/studies/update")
        @WithMockUser
        void updateStudyPost() throws Exception {
            //given
            Long studyPostId = 1L;
            Long userCamInfoId = 1L;
            StudyPostRequest studyPostRequest = new StudyPostRequest("title", "content", StudyStatus.RECRUITING);
            StudyPostResponse studyPostResponse = new StudyPostResponse(1L, "title", "content", StudyStatus.RECRUITING, null, null);

            //when
            when(studyPostService.updateStudyPost(any(StudyPostRequest.class), eq(studyPostId), eq(userCamInfoId)))
                    .thenReturn(studyPostResponse);

            //then
            mockMvc.perform(put("/api/studies/update")
                            .param("studyPostId", studyPostId.toString())
                            .param("userCamInfoId", userCamInfoId.toString())
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(studyPostRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.id").value(1L))
                    .andExpect(jsonPath("$.result.title").value("title"))
                    .andExpect(jsonPath("$.result.content").value("content"))
                    .andExpect(jsonPath("$.result.studyStatus").value("RECRUITING"))
                    .andDo(print());
        }

        @Test
        @DisplayName("PUT /api/studies/update - 접근 권한 없음 실패")
        @WithMockUser
        void updateStudyPostUnauthorized() throws Exception {
            // given
            Long studyPostId = 1L;
            Long userCamInfoId = 2L;
            StudyPostRequest updateRequest = new StudyPostRequest("수정된 제목", "수정된 내용", StudyStatus.IN_PROGRESS);

            // when
            when(studyPostService.updateStudyPost(any(StudyPostRequest.class), eq(studyPostId), eq(userCamInfoId)))
                    .thenThrow(new IllegalArgumentException("접근 권한이 없습니다."));

            // then
            mockMvc.perform(put("/api/studies/update")
                            .with(csrf())
                            .param("studyPostId", studyPostId.toString())
                            .param("userCamInfoId", userCamInfoId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("스터디 게시글 삭제")
    @WithMockUser
    class DeleteStudyPost {

        @Test
        @DisplayName("DELETE /api/studies/delete")
        void deleteStudyPost() throws Exception {
            // given
            Long studyPostId = 1L;
            Long userCamInfoId = 1L;
            StudyPostResponse studyPostResponse = new StudyPostResponse(1L, "title", "content", StudyStatus.RECRUITING, null, null);

            // when
            when(studyPostService.deleteStudyPost(studyPostId, userCamInfoId))
                    .thenReturn(studyPostResponse);

            // then
            mockMvc.perform(delete("/api/studies/delete")
                            .param("studyPostId", studyPostId.toString())
                            .param("userCamInfoId", userCamInfoId.toString())
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.id").value(1L))
                    .andExpect(jsonPath("$.result.title").value("title"))
                    .andExpect(jsonPath("$.result.content").value("content"))
                    .andExpect(jsonPath("$.result.studyStatus").value("RECRUITING"))
                    .andDo(print());
        }

        @Test
        @DisplayName("DELETE /api/studies/delete - 접근 권한 없음 실패")
        void deleteStudyPostUnauthorized() throws Exception {
            // given
            Long studyPostId = 1L;
            Long userCamInfoId = 2L;

            // when
            when(studyPostService.deleteStudyPost(studyPostId, userCamInfoId))
                    .thenThrow(new IllegalArgumentException("접근 권한이 없습니다."));

            // then
            mockMvc.perform(delete("/api/studies/delete")
                            .with(csrf())
                            .param("studyPostId", studyPostId.toString())
                            .param("userCamInfoId", userCamInfoId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andDo(print());
        }
    }
}