package se.sowl.stitchapi.major.contoller;

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
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.major.dto.request.MajorRequest;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchapi.major.service.MajorService;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MajorController.class)
class MajorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MajorService majorService;

    private List<MajorListResponse> majorList;

    @BeforeEach
    void setUp(){
        MajorListResponse major1 = new MajorListResponse(1L, "컴퓨터공학과", null);
        MajorListResponse major2 = new MajorListResponse(2L, "전자공학과", null);
        MajorListResponse major3 = new MajorListResponse(3L, "기계공학과", null);

        majorList = Arrays.asList(major1, major2, major3);

    }

    @Test
    @DisplayName("GET /api/majors/list")
    @WithMockUser
    void getMajorList() throws Exception{
        //given

        //when
        when(majorService.getAllMajors()).thenReturn(majorList);

        //then
        mockMvc.perform(get("/api/majors/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(3))
                .andExpect(jsonPath("$.result[0].name").value("컴퓨터공학과"))
                .andExpect(jsonPath("$.result[1].name").value("전자공학과"))
                .andExpect(jsonPath("$.result[2].name").value("기계공학과"))
                .andDo(print());
    }

    @Nested
    @DisplayName("전공 상세 조회")
    class getMajorDetail{

        @Test
        @DisplayName("GET /api/majors/detail - 전공 상세 조회 성공")
        @WithMockUser
        void getMajorDetailSuccess() throws Exception{
            //given
            MajorDetailResponse majorDetail = new MajorDetailResponse(1L, "컴퓨터공학과", null);

            //when
            when(majorService.getMajorDetail(1L)).thenReturn(majorDetail);

            //then
            mockMvc.perform(get("/api/majors/detail")
                            .param("majorId", "1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.name").value("컴퓨터공학과"))
                    .andDo(print());
        }

        @Test
        @DisplayName("GET /api/majors/detail - 전공 상세 조회 실패")
        @WithMockUser
        void getMajorDetailFail() throws Exception{
            //given

            //when
            when(majorService.getMajorDetail(9999L)).thenThrow(new IllegalArgumentException("존재하지 않는 전공입니다."));

            //then
            mockMvc.perform(get("/api/majors/detail")
                            .param("majorId", "9999")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 전공입니다."))
                    .andDo(print());
        }
    }
}