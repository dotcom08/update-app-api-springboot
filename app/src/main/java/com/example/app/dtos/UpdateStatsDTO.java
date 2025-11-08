package com.example.app.dtos;

import com.example.app.models.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatsDTO {
    private String Version;
    private Map<Platform, Integer> usersCount;
    private Double globalUpdateRate;
}
