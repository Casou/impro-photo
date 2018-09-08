package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JingleDto {

    private String nom;
    private String folder;
    private String path;

    public JingleDto(String nom, String nomFolder) {
        this.nom = nom;
        this.folder = nomFolder;
        this.path = FileUtils.formatPathWithCharacter("/handler/jingles/" + nomFolder + "/" + nom, "/");
    }

}
