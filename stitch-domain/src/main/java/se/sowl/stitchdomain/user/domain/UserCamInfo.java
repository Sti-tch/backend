package se.sowl.stitchdomain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.study.domain.StudyContent;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.domain.StudyPostComment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user_cam_info")
public class UserCamInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(nullable = false)
    private String campusEmail;

    @Column(name = "is_major_skipped")
    private Boolean isMajorSkipped = false;

    @OneToMany(mappedBy = "userCamInfo")
    private List<StudyPost> studyPosts = new ArrayList<>();

    @OneToMany(mappedBy = "userCamInfo")
    private List<StudyMember> studyMembers = new ArrayList<>();

    @OneToMany(mappedBy = "userCamInfo")
    private List<StudyPostComment> studyPostComments = new ArrayList<>();

    @OneToMany(mappedBy = "userCamInfo")
    private List<StudyContent> studyContents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public UserCamInfo(User user, Campus campus, Major major, String campusEmail, Boolean isMajorSkipped) {
        this.setUser(user);
        this.campus = campus;
        this.major = major;
        this.campusEmail = campusEmail;
        this.isMajorSkipped = (isMajorSkipped != null) ? isMajorSkipped : false;
    }

    public boolean isMajorSkipped() {
        return isMajorSkipped != null ? isMajorSkipped : false;
    }


    private void setUser(User user) {
        this.user = user;
        if (user!=null) {
            user.setUserCamInfo(this);
        }
    }

    // 바로 전공을 선택하지 않은 경우
    public void setMajorSkipped(Boolean isMajorSkipped) {
        this.isMajorSkipped = (isMajorSkipped != null) ? isMajorSkipped : false;
    }

    // 전공을 선택할 때 호출
    public void assignMajor(Major major) {
        this.major = major;
        this.isMajorSkipped = false;
    }
}

