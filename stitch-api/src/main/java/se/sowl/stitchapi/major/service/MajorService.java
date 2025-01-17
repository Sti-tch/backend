package se.sowl.stitchapi.major.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.MajorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional
    public List<MajorListResponse> getAllMajors(){
        return majorRepository.findAll().stream()
                .map(MajorListResponse::from)
                .toList();
    }

    @Transactional
    public MajorResponse createMajor(String name){
        if(majorRepository.existsByName(name)){
            throw new MajorException.DuplicateMajorNameException();
        }

        Major major = Major.builder()
                .name(name)
                .build();

        Major savedMajor = majorRepository.save(major);
        return MajorResponse.from(savedMajor);
    }

    @Transactional
    public MajorDetailResponse getMajorDetail(Long majorId){
        Major major = majorRepository.findById(majorId)
                .orElseThrow(MajorException.MajorNotFoundException::new);
        return MajorDetailResponse.from(major);
    }
}
