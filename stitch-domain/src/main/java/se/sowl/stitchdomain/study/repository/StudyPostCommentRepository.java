package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyPostComment;

import java.util.List;

public interface StudyPostCommentRepository extends JpaRepository<StudyPostComment, Long> {
    List<StudyPostComment> findByStudyPostId(Long studyPostId);
}
