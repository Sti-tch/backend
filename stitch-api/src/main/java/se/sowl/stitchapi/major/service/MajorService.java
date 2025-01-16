package se.sowl.stitchapi.major.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.major.dto.MajorResponse;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.MajorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional
    public List<MajorResponse> getAllMajors(){
        return majorRepository.findAll().stream()
                .map(MajorResponse::from)
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
    public MajorResponse getMajorDetail(Long majorId){
        Major major = majorRepository.findById(majorId)
                .orElseThrow(MajorException.MajorNotFoundException::new);
        return MajorResponse.from(major);
    }
}
