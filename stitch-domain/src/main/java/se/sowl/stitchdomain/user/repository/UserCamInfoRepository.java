package se.sowl.stitchdomain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

public interface UserCamInfoRepository extends JpaRepository<UserCamInfo, Long> {

}
