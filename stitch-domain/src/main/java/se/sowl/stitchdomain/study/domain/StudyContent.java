package se.sowl.stitchdomain.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import se.sowl.stitchdomain.study.enumm.ContentType;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_post_id")
    private StudyPost studyPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cam_info_id")
    private UserCamInfo userCamInfo;

    @Enumerated(EnumType.STRING)
    private ContentType studyContentType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public StudyContent(String content, String title, StudyPost studyPost, UserCamInfo userCamInfo, ContentType studyContentType) {
        this.content = content;
        this.title = title;
        this.studyPost = studyPost;
        this.userCamInfo = userCamInfo;
        this.studyContentType = studyContentType;
    }

    public void updateContent(String title,String content, ContentType contentType) {
        this.title = title;
        this.content = content;
        this.studyContentType = contentType;
    }

}
