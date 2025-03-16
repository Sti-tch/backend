package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyMember;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
}
