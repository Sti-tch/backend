package se.sowl.stitchdomain.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.sowl.stitchdomain.school.domain.Campus;

import java.util.Optional;

public interface CampusRepository extends JpaRepository<Campus, Long> {

    @Query("SELECT c FROM Campus c WHERE c.name = :name")
    Optional<Campus> findByName(@Param("name") String name);


}
