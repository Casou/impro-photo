package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.domain.Musique;
import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class UploadedFileDto {

    private File copiedFile;
    private String name;
    private String fileName;

    public Musique toMusique() {
        return Musique.builder()
                .name(this.name)
                .fileName(this.fileName)
                .build();
    }

}
