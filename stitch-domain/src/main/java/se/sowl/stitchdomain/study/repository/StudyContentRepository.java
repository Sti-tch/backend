package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyContent;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.ContentType;

import java.util.List;

public interface StudyContentRepository extends JpaRepository<StudyContent, Long> {

    // 특정 스터디 포스트에 속한 모든 컨텐츠를 생성일자 기준 내림차순으로 조회
    List<StudyContent> findByStudyPostOrderByCreatedAtDesc(StudyPost studyPost);
}
