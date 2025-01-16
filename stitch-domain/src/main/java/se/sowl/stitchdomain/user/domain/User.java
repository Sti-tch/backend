package se.sowl.stitchdomain.user.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean campusCertified = false;

    @Column(nullable = false)
    private String provider;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCamInfo userCamInfo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String name, String nickname, String email, String provider, boolean campusCertified) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.campusCertified = campusCertified;
    }

    public void certifyCampus() {
        this.campusCertified = true;
    }

    void setUserCamInfo(UserCamInfo userCamInfo) {
        this.userCamInfo = userCamInfo;
    }
}