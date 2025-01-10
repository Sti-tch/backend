package se.sowl.stitchdomain.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.school.domain.Campus;

import java.util.Optional;

public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findByName(String name);
}
