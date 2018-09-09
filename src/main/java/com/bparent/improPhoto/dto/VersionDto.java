package com.bparent.improPhoto.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class VersionDto implements Comparable<VersionDto> {

    private BigInteger id;
    private String buildNumber;
    private Date buildDate;
    private String buildDescription;
    private String urlDownload;

    @Override
    public int compareTo(VersionDto dto) {
        return dto.getBuildDate() == null ? 1 :
                this.buildDate == null ? -1 :
                        this.buildDate.compareTo(dto.getBuildDate());
    }
}
