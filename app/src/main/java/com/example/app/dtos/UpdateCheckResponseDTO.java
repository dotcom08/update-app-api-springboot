package com.example.app.dtos;

import com.example.app.models.UpdateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCheckResponseDTO {

    private boolean updateAvailable;
    private String latestVersion;
    private String downloadUrl;
    private String changelog;
    private UpdateType updateType;
    private boolean mandatory;
    private Long fileSize;
    private String checksum;

}
