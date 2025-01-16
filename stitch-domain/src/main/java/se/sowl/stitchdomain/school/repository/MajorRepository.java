package se.sowl.stitchdomain.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.school.domain.Major;

import java.util.Optional;
public interface MajorRepository extends JpaRepository<Major, Long> {
    boolean existsByName(String name);
}
