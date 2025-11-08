package com.example.app.dtos;

import com.example.app.models.Platform;
import com.example.app.models.UpdateStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateLogRequestDTO {
    @NotNull
    private String userId;

    @NotNull
    private String fromVersion;

    @NotNull
    private String toVersion;

    @NotNull
    private Platform platform;

    private UpdateStatus status;
    private String errorMessage;
}
