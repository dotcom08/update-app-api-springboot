package com.example.app.repositories;

import com.example.app.models.AppVersion;
import com.example.app.models.Platform;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    @Query("SELECT v FROM AppVersion v WHERE v.platform = :platform AND v.isActive = true ORDER BY v.releaseDate DESC")
     List<AppVersion> findActiveVersionByPlatform(
             @Param("platform") Platform platform
     );
    Optional<AppVersion> findTopByPlatformAndIsActiveTrueOrderByReleaseDateDesc(Platform platform);

    Optional<AppVersion> findByVersionAndPlatform(String version, Platform platform);

    @Query("SELECT v FROM AppVersion v WHERE v.version = :version AND v.platform = :platform AND v.isActive = true")
    Optional<AppVersion> findActiveByVersionAndPlatform(@Param("version") String version, @Param("platform") Platform platform);

    List<AppVersion> findByIsActiveTrue();
    List<AppVersion> findByPlatform(Platform platform);
}


