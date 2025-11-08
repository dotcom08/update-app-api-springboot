package com.example.app.services;

import com.example.app.models.Platform;
import com.example.app.models.UserDevice;
import com.example.app.repositories.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDeviceService {

    @Autowired
    private final UserDeviceRepository userDeviceRepository;

    public List<UserDevice> getAllUserDevices() {
        return userDeviceRepository.findAll();
    }

    public UserDevice getUserDeviceById(Long id) {
        return userDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User device not found with id: " + id));
    }

    public UserDevice getUserDeviceByUserId(String userId) {
        return userDeviceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User device not found for user: " + userId));
    }

    public List<UserDevice> getUserDevicesByPlatform(Platform platform) {
        return userDeviceRepository.findByPlatform(platform);
    }

    public List<UserDevice> getUserDevicesByVersion(String version) {
        return userDeviceRepository.findByCurrentVersion(version);
    }

    public UserDevice createOrUpdateUserDevice(UserDevice userDevice) {
        // If a user device exists, update it; otherwise create new
        return userDeviceRepository.findByUserId(userDevice.getUserId())
                .map(existingDevice -> {
                    existingDevice.setPlatform(userDevice.getPlatform());
                    existingDevice.setCurrentVersion(userDevice.getCurrentVersion());
                    existingDevice.setLastSeen(LocalDateTime.now());
                    return userDeviceRepository.save(existingDevice);
                })
                .orElseGet(() -> {
                    userDevice.setLastSeen(LocalDateTime.now());
                    userDevice.setCreatedAt(LocalDateTime.now());
                    return userDeviceRepository.save(userDevice);
                });
    }

    public void deleteUserDevice(Long id) {
        if (!userDeviceRepository.existsById(id)) {
            throw new RuntimeException("User device not found with id: " + id);
        }
        userDeviceRepository.deleteById(id);
    }

    public Map<String, Object> getUserDeviceStats() {
        Map<String, Object> stats = new HashMap<>();

        Long totalDevices = userDeviceRepository.count();
        stats.put("totalDevices", totalDevices);

        // Count by platform
        Map<String, Long> devicesByPlatform = new HashMap<>();
        for (String platform : List.of("ANDROID", "IOS", "WINDOWS", "MACOS", "LINUX", "WEB")) {
            Long count = userDeviceRepository.countByPlatform(platform);
            devicesByPlatform.put(platform, count);
        }
        stats.put("devicesByPlatform", devicesByPlatform);

        // Count by version
        List<Object[]> versionCounts = userDeviceRepository.countDevicesByVersion();
        Map<String, Long> devicesByVersion = new HashMap<>();
        for (Object[] result : versionCounts) {
            devicesByVersion.put((String) result[0], (Long) result[1]);
        }
        stats.put("devicesByVersion", devicesByVersion);

        return stats;
    }
}
