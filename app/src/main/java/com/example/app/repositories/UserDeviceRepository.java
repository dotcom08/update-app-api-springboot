package com.example.app.repositories;


import com.example.app.models.Platform;
import com.example.app.models.UserDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByUserId(String userId);

    List<UserDevice> findByPlatformAndCurrentVersion(Platform platform, String version);

    @Query("SELECT COUNT(ud) FROM UserDevice ud WHERE ud.currentVersion = :version")
    Long countUsersByVersion(@Param("version") String version);

    @Query("SELECT ud.currentVersion, COUNT(ud) FROM UserDevice ud WHERE ud.platform = :platform GROUP BY ud.currentVersion")
    List<Object[]> countUsersByVersionAndPlatform(@Param("platform") Platform platform);

    @Query("SELECT COUNT(ud) FROM UserDevice ud WHERE ud.platform = :platform")
    Long countUsersByPlatform(@Param("platform") Platform platform);


}
