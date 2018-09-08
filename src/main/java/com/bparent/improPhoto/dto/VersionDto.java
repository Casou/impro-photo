package com.bparent.improPhoto.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class VersionDto {

    private BigInteger id;
    private String buildNumber;
    private Date buildDate;
    private String buildDescription;
    private String urlDownload;

}
