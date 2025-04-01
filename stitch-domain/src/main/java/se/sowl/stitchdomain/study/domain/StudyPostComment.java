package se.sowl.stitchdomain.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_post_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyPostComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_post_id")
    private StudyPost studyPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cam_info_id")
    private UserCamInfo userCamInfo;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public StudyPostComment(String content, StudyPost studyPost, UserCamInfo userCamInfo) {
        this.content = content;
        this.studyPost = studyPost;
        this.userCamInfo = userCamInfo;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
