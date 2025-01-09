package se.sowl.stitchdomain.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.school.domain.Major;

public interface MajorRepository extends JpaRepository<Major, Long> {
}
