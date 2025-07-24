package se.sowl.stitchapi.univcert.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.stitchdomain.school.repository.CampusRepository;

@Service
@RequiredArgsConstructor
public class UniversityValidationService {

    private final CampusRepository campusRepository;

    public boolean isValidUniversity(String univName) {
        return campusRepository.findByName(univName).isPresent();
    }
}