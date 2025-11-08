package com.example.app.controllers;


import com.example.app.models.Platform;
import com.example.app.models.UserDevice;
import com.example.app.services.UserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-devices")
@Tag(name = "User Devices", description = "APIs for managing user device information and tracking")
public class UserDeviceController {
    @Autowired
    private final UserDeviceService userDeviceService;

    @Operation(
            summary = "Get all user devices",
            description = "Retrieve a list of all registered user devices across all platforms"
    )
    @GetMapping
    public ResponseEntity<List<UserDevice>> getAllUserDevices() {
        List<UserDevice> devices = userDeviceService.getAllUserDevices();
        return ResponseEntity.ok(devices);
    }

    @Operation(
            summary = "Get user device by ID",
            description = "Retrieve a specific user device by its unique identifier"
    )

    @GetMapping("/{id}")
    public ResponseEntity<UserDevice> getUserDeviceById(
            @PathVariable Long id) {
        UserDevice device = userDeviceService.getUserDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @Operation(
            summary = "Get user device by user ID",
            description = "Retrieve user device information using the user's unique identifier"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDevice> getUserDeviceByUserId(
            @PathVariable String userId) {
        UserDevice device = userDeviceService.getUserDeviceByUserId(userId);
        return ResponseEntity.ok(device);
    }

    @Operation(
            summary = "Get user devices by platform",
            description = "Retrieve all user devices for a specific platform"
    )
    @GetMapping("/platform/{platform}")
    public ResponseEntity<List<UserDevice>> getUserDevicesByPlatform(
            @PathVariable Platform platform) {
        List<UserDevice> devices = userDeviceService.getUserDevicesByPlatform(platform);
        return ResponseEntity.ok(devices);
    }

    @Operation(
            summary = "Get user devices by version",
            description = "Retrieve all user devices running a specific application version"
    )
    @GetMapping("/version/{version}")
    public ResponseEntity<List<UserDevice>> getUserDevicesByVersion(
            @PathVariable String version) {
        List<UserDevice> devices = userDeviceService.getUserDevicesByVersion(version);
        return ResponseEntity.ok(devices);
    }

    @Operation(
            summary = "Create or update user device",
            description = "Register a new user device or update existing device information"
    )

    @PostMapping
    public ResponseEntity<UserDevice> createOrUpdateUserDevice(
            @RequestBody UserDevice userDevice) {
        UserDevice savedDevice = userDeviceService.createOrUpdateUserDevice(userDevice);
        return ResponseEntity.ok(savedDevice);
    }

    @Operation(
            summary = "Delete user device",
            description = "Remove a user device from the system"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDevice(
            @PathVariable Long id) {
        userDeviceService.deleteUserDevice(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get user device statistics",
            description = "Retrieve statistics about user devices distribution"
    )
    @GetMapping("/stats/summary")
    public ResponseEntity<Map<String, Object>> getUserDeviceStats() {
        Map<String, Object> stats = userDeviceService.getUserDeviceStats();
        return ResponseEntity.ok(stats);
    }
}
