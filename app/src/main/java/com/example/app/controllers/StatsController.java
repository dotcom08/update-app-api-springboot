package com.example.app.controllers;

import com.example.app.dtos.UpdateStatsDTO;
import com.example.app.services.AppVersionService;
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
public class StatsController {

    @Autowired
    private final AppVersionService appVersionService;

    // GET /api/stats/updates – статистика перехода на новые версии
    @GetMapping("/updates")
    public ResponseEntity<List<UpdateStatsDTO>> getUpdateStats() {
        List<UpdateStatsDTO> stats = appVersionService.getUpdateStats();
        return ResponseEntity.ok(stats);
    }
}
