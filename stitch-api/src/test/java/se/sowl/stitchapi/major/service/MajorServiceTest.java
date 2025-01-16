package se.sowl.stitchapi.major.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.major.dto.MajorResponse;
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
    @DisplayName("전공 생성 테스트")
    class CreateMajorTest{
        @Test
        @DisplayName("전공 생성 성공")
        void createMajorSuccess(){
            //given
            String name = "테스트 전공";

            //when
            MajorResponse majorResponse = majorService.createMajor(name);

            //then
            assertEquals(name, majorResponse.getName());
        }
        @Test
        @DisplayName("이미 존재하는 전공명으로 생성 시 실패 ")
        void createMajorFail(){
            //given
            String name = "컴퓨터공학과";

            //when & then
            assertThrows(MajorException.DuplicateMajorNameException.class, () -> majorService.createMajor(name));
        }
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
            List<MajorResponse> majorResponses = majorService.getAllMajors();

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
            MajorResponse majorResponse = majorService.getMajorDetail(majorId);

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