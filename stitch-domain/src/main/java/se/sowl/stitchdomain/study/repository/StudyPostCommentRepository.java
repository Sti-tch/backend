package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyPostComment;

public interface StudyPostCommentRepository extends JpaRepository<StudyPostComment, Long> {
}
