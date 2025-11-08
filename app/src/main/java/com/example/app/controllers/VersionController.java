package com.example.app.controllers;


import com.example.app.dtos.VersionRequestDTO;
import com.example.app.models.AppVersion;
import com.example.app.models.Platform;
import com.example.app.services.AppVersionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/versions")
@Tag(name = "Version Management", description = "APIs for managing application versions and updates")
public class VersionController {
    @Autowired
    private final AppVersionService appVersionService;


    @Operation(
            summary = "Get all versions",
            description = "Retrieve a list of all application versions across all platforms"
    )
    // GET /api/versions - получить все версии
    @GetMapping
    public ResponseEntity<List<AppVersion>> getAllVersions() {
        List<AppVersion> versions = appVersionService.getAllVersions();
        return ResponseEntity.ok(versions);
    }


    @Operation(
            summary = "Get active versions",
            description = "Retrieve only active application versions that are available for updates"
    )
    // GET /api/versions/active - получить только активные версии
    @GetMapping("/active")
    public ResponseEntity<List<AppVersion>> getActiveVersions() {
        List<AppVersion> activeVersions = appVersionService.getActiveVersions();
        return ResponseEntity.ok(activeVersions);
    }

    @Operation(
            summary = "Get versions by platform",
            description = "Retrieve application versions for a specific platform"
    )
    // GET /api/versions/platform/{platform} - получить версии по платформе
    @GetMapping("/platform/{platform}")
    public ResponseEntity<List<AppVersion>> getVersionsByPlatform(@PathVariable Platform platform) {
        List<AppVersion> versions = appVersionService.getVersionsByPlatform(platform);
        return ResponseEntity.ok(versions);
    }

    @Operation(
            summary = "Get version by ID",
            description = "Retrieve a specific application version by its unique identifier"
    )
    // GET /api/versions/{id} - получить версию по ID
    @GetMapping("/{id}")
    public ResponseEntity<AppVersion> getVersionById(@PathVariable Long id) {
        AppVersion version = appVersionService.getVersionById(id);
        return ResponseEntity.ok(version);
    }

    @Operation(
            summary = "Create new version",
            description = "Add a new application version to the update system"
    )
    // POST /api/versions – добавить новую версию
    @PostMapping
    public ResponseEntity<AppVersion> addVersion(@RequestBody VersionRequestDTO request) {
        AppVersion version = appVersionService.addVersion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(version);
    }


    @Operation(
            summary = "Get latest version for platform",
            description = "Retrieve the most recent active version for a specific platform"
    )
    // GET /api/versions/latest?platform=android – последняя версия для платформы
    @GetMapping("/latest")
    public ResponseEntity<AppVersion> getLatestVersion(
            @RequestParam Platform platform) {

        AppVersion latestVersion = appVersionService.getLatestVersion(platform);
        return ResponseEntity.ok(latestVersion);
    }

}
