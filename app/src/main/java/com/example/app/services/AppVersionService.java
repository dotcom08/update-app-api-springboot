package com.example.app.services;


import com.example.app.dtos.UpdateCheckResponseDTO;
import com.example.app.dtos.UpdateLogRequestDTO;
import com.example.app.dtos.UpdateStatsDTO;
import com.example.app.dtos.VersionRequestDTO;
import com.example.app.models.*;
import com.example.app.repositories.AppVersionRepository;
import com.example.app.repositories.UpdateLogRepository;
import com.example.app.repositories.UserDeviceRepository;
import com.example.app.utils.Version;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;



@RequiredArgsConstructor
@Service
public class AppVersionService {

    @Autowired
    private final AppVersionRepository appVersionRepository;

    @Autowired
    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    private final UpdateLogRepository updateLogRepository;

    // get all versions
    public List<AppVersion> getAllVersions() {
        return appVersionRepository.findAll();
    }

    // get all active versions
    public List<AppVersion> getActiveVersions() {
        return appVersionRepository.findByIsActiveTrue();
    }

    // get all versions by platform
    public List<AppVersion> getVersionsByPlatform(Platform platform) {
        return appVersionRepository.findByPlatform(platform);
    }

    // get versions by id
    public AppVersion getVersionById(Long id) {
        return appVersionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Version not found with id: " + id));
    }

    public AppVersion addVersion(VersionRequestDTO request) {

        Optional<AppVersion> existingVersion = appVersionRepository
                .findByVersionAndPlatform(request.getVersion(), request.getPlatform());

        if (existingVersion.isPresent()) {
            throw new RuntimeException("Version already exists for this platform");
        }

        AppVersion version = new AppVersion();
        version.setVersion(request.getVersion());
        version.setPlatform(request.getPlatform());
        version.setChangelog(request.getChangeLog());
        version.setUpdateType(request.getUpdateType());
        version.setDownloadUrl(request.getDownloadUrl());
        version.setFileSize(request.getFileSize());
        version.setReleaseDate(LocalDateTime.now());
        version.setActive(true);

        return appVersionRepository.save(version);
    }

    public AppVersion getLatestVersion(Platform platform) {
        return appVersionRepository
                .findTopByPlatformAndIsActiveTrueOrderByReleaseDateDesc(platform)
                .orElseThrow(() -> new RuntimeException("No active version found for platform: " + platform));
    }

    public UpdateCheckResponseDTO checkForUpdate(String userId, String currentVersion, Platform platform) {

        updateUserDevice(userId, currentVersion, platform);

        AppVersion latestVersion = getLatestVersion(platform);
        boolean updateAvailable = isNewerVersion(currentVersion, latestVersion.getVersion());

        UpdateCheckResponseDTO response = new UpdateCheckResponseDTO();
        response.setUpdateAvailable(updateAvailable);

        if (updateAvailable) {
            response.setLatestVersion(latestVersion.getVersion());
            response.setDownloadUrl(latestVersion.getDownloadUrl());
            response.setChangelog(latestVersion.getChangelog());
            response.setUpdateType(latestVersion.getUpdateType());
            response.setMandatory(latestVersion.getUpdateType() == UpdateType.MANDATORY);
            response.setFileSize(latestVersion.getFileSize());
        }

        return response;
    }
    public void logUpdate(UpdateLogRequestDTO request) {
        UpdateLog log = new UpdateLog();
        log.setUserId(request.getUserId());
        log.setPlatform(request.getPlatform());
        log.setFromVersion(request.getFromVersion());
        log.setToVersion(request.getToVersion());
        log.setStatus(request.getStatus());
        log.setErrorMessage(request.getErrorMessage());
        log.setUpdateDate(LocalDateTime.now());

        updateLogRepository.save(log);

        if (request.getStatus() == UpdateStatus.SUCCESS) {
            updateUserVersion(request.getUserId(), request.getToVersion());
        }
    }


    public List<UpdateStatsDTO> getUpdateStats() {
        List<UpdateStatsDTO> stats = new ArrayList<>();

        List<AppVersion> activeVersions = appVersionRepository.findAll()
                .stream()
                .filter(AppVersion::isActive)
                .toList();

        long totalUsers = userDeviceRepository.count();

        for (AppVersion version : activeVersions) {
            UpdateStatsDTO stat = new UpdateStatsDTO();
            stat.setVersion(version.getVersion());

            // count user by platform on this version
            Map<Platform, Integer> usersCount = new HashMap<>();
            for (Platform platform : Platform.values()) {
                int count = userDeviceRepository.findByPlatformAndCurrentVersion(platform, version.getVersion()).size();
                usersCount.put(platform, count);
            }
            stat.setUsersCount(usersCount);

            // calculate global update rate
            Long usersWithVersion = userDeviceRepository.countUsersByVersion(version.getVersion());
            Double updateRate = totalUsers > 0 ? (double) usersWithVersion / totalUsers * 100 : 0.0;
            stat.setGlobalUpdateRate(updateRate);

            stats.add(stat);
        }

        return stats;
    }

    private void updateUserDevice(String userId, String currentVersion, Platform platform) {
        UserDevice device = userDeviceRepository.findByUserId(userId)
                .orElse(new UserDevice(userId, platform, currentVersion));

        device.setCurrentVersion(currentVersion);
        device.setPlatform(platform);
        device.setLastSeen(LocalDateTime.now());

        userDeviceRepository.save(device);
    }


    private void updateUserVersion(String userId, String newVersion) {
        userDeviceRepository.findByUserId(userId).ifPresent(device -> {
            device.setCurrentVersion(newVersion);
            device.setLastSeen(LocalDateTime.now());
            userDeviceRepository.save(device);
        });
    }

    private boolean isNewerVersion(String currentVersion, String latestVersion) {

        Version current = new Version(currentVersion);
        Version latest = new Version(latestVersion);
        return latest.compareTo(current) > 0;
    }




}
