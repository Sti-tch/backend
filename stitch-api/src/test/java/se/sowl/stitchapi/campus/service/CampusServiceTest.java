package se.sowl.stitchapi.campus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.campus.dto.CampusListResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.repository.CampusRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CampusServiceTest {

    @Autowired
    private CampusService campusService;

    @Autowired
    private CampusRepository campusRepository;

    private Campus testCampus;

    @BeforeEach
    void setUp() {
        testCampus = Campus.builder()
                .name("테스트 캠퍼스")
                .build();
        testCampus = campusRepository.save(testCampus);
    }

    @Nested
    @DisplayName("모든 캠퍼스 조회 테스트")
    class GetAllCampusesTest{
        @Test
        @DisplayName("모든 캠퍼스 조회 성공")
        void getAllCampusesSuccess(){
            //given
            for (int i = 0; i < 5; i++) {
                Campus campus = Campus.builder()
                        .name("테스트 캠퍼스" + i)
                        .build();
                campusRepository.save(campus);
            }

            //when
            List<CampusListResponse> campusListResponses = campusService.getAllCampuses();

            //then
            assertEquals(6, campusListResponses.size());
        }
    }


}