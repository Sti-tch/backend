package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyPost;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
}
