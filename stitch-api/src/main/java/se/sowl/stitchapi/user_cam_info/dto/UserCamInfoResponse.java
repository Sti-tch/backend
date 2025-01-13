package se.sowl.stitchapi.user_cam_info.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.user.domain.User_Cam_Info;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCamInfoResponse {
    private Long id;
    private Long userId;
    private Long campusId;
    private Long majorId;
    private String userName;
    private String campusName;
    private String majorName;
    private String campusEmail;
    private LocalDateTime createdAt;

    public static UserCamInfoResponse from(User_Cam_Info userCamInfo) {
        return new UserCamInfoResponse(
                userCamInfo.getId(),
                userCamInfo.getUser().getId(),
                userCamInfo.getCampus().getId(),
                userCamInfo.getMajor().getId(),
                userCamInfo.getUser().getName(),
                userCamInfo.getCampus().getName(),
                userCamInfo.getMajor().getName(),
                userCamInfo.getCampusEmail(),
                userCamInfo.getCreatedAt()
        );
    }
}
