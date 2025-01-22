package se.sowl.stitchapi.campus.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.sowl.stitchapi.campus.dto.response.CampusListResponse;
import se.sowl.stitchapi.campus.service.CampusService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CampusController.class)
class CampusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampusService campusService;

    private List<CampusListResponse> campusList;

    @BeforeEach
    void setUp(){
        CampusListResponse campus1 = new CampusListResponse(1L, "서울대학교", null);
        CampusListResponse campus2 = new CampusListResponse(2L, "부산대학교", null);
        CampusListResponse campus3 = new CampusListResponse(3L, "대전대학교", null);

        campusList = Arrays.asList(campus1, campus2, campus3);
    }

    @Test
    @DisplayName("GET /api/campus/list")
    @WithMockUser
    void getCampusList() throws Exception{
        //given

        //when
        when(campusService.getAllCampuses()).thenReturn(campusList);

        //then
        mockMvc.perform(get("/api/campus/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(3))
                .andExpect(jsonPath("$.result[0].id").value(1))
                .andExpect(jsonPath("$.result[0].name").value("서울대학교"))
                .andExpect(jsonPath("$.result[1].id").value(2))
                .andExpect(jsonPath("$.result[1].name").value("부산대학교"))
                .andExpect(jsonPath("$.result[2].id").value(3))
                .andExpect(jsonPath("$.result[2].name").value("대전대학교"))
                .andDo(print());
    }


}