package se.sowl.stitchapi.major.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.MajorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MajorServiceTest {

    @Autowired
    private MajorService majorService;

    @Autowired
    private MajorRepository majorRepository;

    private Major testMajor;

    @BeforeEach
    void setUp() {
        testMajor = Major.builder()
                .name("컴퓨터공학과")
                .build();
        testMajor = majorRepository.save(testMajor);
    }


    @Nested
    @DisplayName("모든 전공 조회 테스트")
    class GetAllMajorsTest{
        @Test
        @DisplayName("모든 전공 조회 성공")
        void getAllMajorsSuccess(){
            //given
            for (int i = 1; i <= 5; i++) {
                Major major = Major.builder()
                        .name("테스트 전공" + i)
                        .build();
                majorRepository.save(major);
            }
            //when
            List<MajorListResponse> majorResponses = majorService.getAllMajors();

            //then
            assertEquals(6, majorResponses.size());
        }
    }

    @Nested
    @DisplayName("전공 상세 조회 테스트")
    class GetMajorDetailTest{
        @Test
        @DisplayName("전공 상세 조회 성공")
        void getMajorDetailSuccess(){
            //given
            Long majorId = testMajor.getId();

            //when
            MajorDetailResponse majorResponse = majorService.getMajorDetail(majorId);

            //then
            assertEquals(testMajor.getName(), majorResponse.getName());
        }

        @Test
        @DisplayName("존재하지 않는 전공 조회 시 예외 발생")
        void getMajorDetailFail(){
            //given
            Long nonExistentMajorId = 99999L;

            //when & then
            assertThrows(MajorException.MajorNotFoundException.class, () -> majorService.getMajorDetail(nonExistentMajorId));
        }
    }


}