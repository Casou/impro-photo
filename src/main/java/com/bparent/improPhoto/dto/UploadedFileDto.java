package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.domain.Musique;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@Builder
public class UploadedFileDto {

    private File destinationFile;
    private Boolean isFile;
    private String name;
    private String fileName;
    private String originalPath;
    @Builder.Default
    private List<UploadedFileDto> children = new ArrayList<>();
    private Boolean uploadSuccess;

    public Musique toMusique() {
        return Musique.builder()
                .name(this.name)
                .fileName(this.fileName)
                .build();
    }

    public Stream<UploadedFileDto> streamChildren() {
        return Stream.concat(Stream.of(this), this.getChildren().stream().map(child -> child.streamChildren()).flatMap(x -> x));
    }

}
