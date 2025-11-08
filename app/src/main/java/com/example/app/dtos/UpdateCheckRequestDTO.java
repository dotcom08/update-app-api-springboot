package com.example.app.dtos;

import com.example.app.models.Platform;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateCheckRequestDTO {
    private String userId;
    private String currentVersion;
    private Platform platform;
}
