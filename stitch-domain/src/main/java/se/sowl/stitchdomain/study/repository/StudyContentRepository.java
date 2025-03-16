package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyContent;

public interface StudyContentRepository extends JpaRepository<StudyContent, Long> {
}
