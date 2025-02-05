package se.sowl.stitchdomain.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import se.sowl.stitchdomain.study.enumm.StudyStatus;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cam_info_id")
    private UserCamInfo userCamInfo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "studyPost")
    private List<StudyMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "studyPost")
    private List<StudyContent> studyContents = new ArrayList<>();

    @OneToMany(mappedBy = "studyPost")
    private List<StudyPostComment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @Builder
    public StudyPost(UserCamInfo userCamInfo, String title, String content, StudyStatus studyStatus) {
        this.userCamInfo = userCamInfo;
        this.title = title;
        this.content = content;
        this.studyStatus = studyStatus;
    }
}
