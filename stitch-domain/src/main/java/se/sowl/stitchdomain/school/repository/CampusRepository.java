package se.sowl.stitchdomain.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.school.domain.Campus;

public interface CampusRepository extends JpaRepository<Campus, Long> {
}
