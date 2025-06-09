package se.sowl.stitchdomain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findByStudyPostId(Long studyPostId);

    /**
     * 특정 스터디에 특정 사용자가 이미 신청했거나 멤버인지 확인
     */
    boolean existsByStudyPostAndUserCamInfo(StudyPost studyPost, UserCamInfo userCamInfo);

    /**
     * 특정 스터디에서 특정 사용자가 특정 역할(리더 등)인지 확인
     */
    boolean existsByStudyPostAndUserCamInfoAndMemberRole(
            StudyPost studyPost, UserCamInfo userCamInfo, MemberRole memberRole);

    /**
     * 특정 스터디에서 특정 사용자의 멤버십 정보 조회
     */
    Optional<StudyMember> findByStudyPostAndUserCamInfo(StudyPost studyPost, UserCamInfo userCamInfo);

    /**
     * 특정 스터디에서 특정 역할(리더 등)을 가진 멤버 조회
     */
    Optional<StudyMember> findByStudyPostAndMemberRole(StudyPost studyPost, MemberRole memberRole);

    /**
     * 특정 스터디에서 특정 사용자의 멤버십 정보 조회 (상태가 승인된 경우)
     */
    List<StudyMember> findByStudyPostAndMemberStatus(StudyPost studyPost, MemberStatus memberStatus);

    List<StudyMember> findByUserCamInfo(UserCamInfo userCamInfo);

    List<StudyMember> findByUserCamInfoAndMemberStatus(UserCamInfo userCamInfo, MemberStatus memberStatus);
}
