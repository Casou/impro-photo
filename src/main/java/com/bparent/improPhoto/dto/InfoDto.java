package com.bparent.improPhoto.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoDto {
    private String applicationName;
    private String applicationVersion;
    private String applicationTimestamp;
}
