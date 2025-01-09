package se.sowl.stitchdomain.school.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.user.domain.User_Cam_Info;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "majors")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "major")
    private List<User_Cam_Info> user_cam_infos = new ArrayList<>();
}
