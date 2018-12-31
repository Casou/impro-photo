package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.domain.Jingle;
import com.bparent.improPhoto.domain.JingleCategory;
import com.bparent.improPhoto.domain.Musique;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@Builder
@ToString(exclude={"children", "parent"})
public class UploadedFileDto {

    private Boolean isFile;
    private String name;
    private String fileName;
    private String originalPath;
    @Builder.Default
    private List<UploadedFileDto> children = new ArrayList<>();
    private UploadedFileDto parent;
    private Boolean uploadSuccess;

    public Musique toMusique() {
        return Musique.builder()
                .name(this.name)
                .fileName(this.fileName)
                .build();
    }

    public Jingle toJingle(JingleCategory category) {
        return Jingle.builder()
                .fileName(this.getFileName())
                .name(this.getName())
                .category(category)
                .build();
    }

    public Stream<UploadedFileDto> streamChildren() {
        return Stream.concat(Stream.of(this), this.getChildren().stream().map(child -> child.streamChildren()).flatMap(x -> x));
    }

    public String getParentPath() {
        String path = "";
        if (this.parent != null) {
            path = this.parent.getParentPath() + this.parent.getFileName() + "/" + path;
        }

        return path;
    }

}
