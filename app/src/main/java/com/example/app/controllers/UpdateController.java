package com.example.app.controllers;

import com.example.app.dtos.UpdateCheckResponseDTO;
import com.example.app.dtos.UpdateLogRequestDTO;
import com.example.app.models.Platform;
import com.example.app.services.AppVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/update")
@Tag(name = "Update Check", description = "APIs for checking updates and logging update activities")
public class UpdateController {

    @Autowired
    private final AppVersionService appVersionService;


    @Operation(
            summary = "Check for application updates",
            description = "Check if a newer version is available for the user's current application version and platform"
    )
    // GET /api/update/check?userId=123&current=1.1.0&platform=ios – проверка обновления
    @GetMapping("/check")
    public ResponseEntity<UpdateCheckResponseDTO> checkForUpdate(
            @RequestParam String userId,
            @RequestParam String current,
            @RequestParam Platform platform) {

        UpdateCheckResponseDTO response = appVersionService.checkForUpdate(userId, current, platform);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Log update activity",
            description = "Record update attempts, successes, failures, or cancellations for analytics and tracking"
    )
    // POST /api/update/log – лог установки обновления
    @PostMapping("/log")
    public ResponseEntity<Void> logUpdate(@RequestBody UpdateLogRequestDTO request) {
        appVersionService.logUpdate(request);
        return ResponseEntity.ok().build();
    }
}
