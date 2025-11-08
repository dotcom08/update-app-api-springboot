package com.example.app.controllers;

import com.example.app.dtos.UpdateStatsDTO;
import com.example.app.services.AppVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stats")
@Tag(name = "Statistics", description = "APIs for retrieving update statistics and analytics")
public class StatsController {

    @Autowired
    private final AppVersionService appVersionService;


    @Operation(
            summary = "Get update statistics",
            description = "Retrieve comprehensive statistics about user adoption rates and version distribution across platforms"
    )
    // GET /api/stats/updates – статистика перехода на новые версии
    @GetMapping("/updates")
    public ResponseEntity<List<UpdateStatsDTO>> getUpdateStats() {
        List<UpdateStatsDTO> stats = appVersionService.getUpdateStats();
        return ResponseEntity.ok(stats);
    }
}
