package se.sowl.stitchdomain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.util.Optional;

public interface UserCamInfoRepository extends JpaRepository<UserCamInfo, Long> {

    Optional<UserCamInfo> findByUser(User user);
}
