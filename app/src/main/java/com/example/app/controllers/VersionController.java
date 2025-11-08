package com.example.app.controllers;


import com.example.app.dtos.VersionRequestDTO;
import com.example.app.models.AppVersion;
import com.example.app.models.Platform;
import com.example.app.services.AppVersionService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/versions")
public class VersionController {
    @Autowired
    private final AppVersionService appVersionService;
    // GET /api/versions - получить все версии
    @GetMapping
    public ResponseEntity<List<AppVersion>> getAllVersions() {
        List<AppVersion> versions = appVersionService.getAllVersions();
        return ResponseEntity.ok(versions);
    }

    // GET /api/versions/active - получить только активные версии
    @GetMapping("/active")
    public ResponseEntity<List<AppVersion>> getActiveVersions() {
        List<AppVersion> activeVersions = appVersionService.getActiveVersions();
        return ResponseEntity.ok(activeVersions);
    }

    // GET /api/versions/platform/{platform} - получить версии по платформе
    @GetMapping("/platform/{platform}")
    public ResponseEntity<List<AppVersion>> getVersionsByPlatform(@PathVariable Platform platform) {
        List<AppVersion> versions = appVersionService.getVersionsByPlatform(platform);
        return ResponseEntity.ok(versions);
    }

    // GET /api/versions/{id} - получить версию по ID
    @GetMapping("/{id}")
    public ResponseEntity<AppVersion> getVersionById(@PathVariable Long id) {
        AppVersion version = appVersionService.getVersionById(id);
        return ResponseEntity.ok(version);
    }

    // POST /api/versions – добавить новую версию
    @PostMapping
    public ResponseEntity<AppVersion> addVersion(@RequestBody VersionRequestDTO request) {
        AppVersion version = appVersionService.addVersion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(version);
    }

    // GET /api/versions/latest?platform=android – последняя версия для платформы
    @GetMapping("/latest")
    public ResponseEntity<AppVersion> getLatestVersion(
            @RequestParam Platform platform) {

        AppVersion latestVersion = appVersionService.getLatestVersion(platform);
        return ResponseEntity.ok(latestVersion);
    }

}
