package se.sowl.stitchapi.campus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.campus.dto.CampusListResponse;
import se.sowl.stitchdomain.school.repository.CampusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campusRepository;

    @Transactional
    public List<CampusListResponse> getAllCampuses(){
        return campusRepository.findAll().stream()
                .map(CampusListResponse::from)
                .toList();
    }
}
