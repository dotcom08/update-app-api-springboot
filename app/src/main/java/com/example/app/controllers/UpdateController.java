package com.example.app.controllers;

import com.example.app.dtos.UpdateCheckResponseDTO;
import com.example.app.dtos.UpdateLogRequestDTO;
import com.example.app.models.Platform;
import com.example.app.services.AppVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/update")
public class UpdateController {

    @Autowired
    private final AppVersionService appVersionService;

    // GET /api/update/check?userId=123&current=1.1.0&platform=ios – проверка обновления
    @GetMapping("/check")
    public ResponseEntity<UpdateCheckResponseDTO> checkForUpdate(
            @RequestParam String userId,
            @RequestParam String current,
            @RequestParam Platform platform) {

        UpdateCheckResponseDTO response = appVersionService.checkForUpdate(userId, current, platform);
        return ResponseEntity.ok(response);
    }

    // POST /api/update/log – лог установки обновления
    @PostMapping("/log")
    public ResponseEntity<Void> logUpdate(@RequestBody UpdateLogRequestDTO request) {
        appVersionService.logUpdate(request);
        return ResponseEntity.ok().build();
    }
}
