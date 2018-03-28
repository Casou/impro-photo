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
    private String path;

    public JingleDto(String nom) {
        this.nom = nom;
        this.path = FileUtils.formatPathWithCharacter("/handler/jingles/" + nom, "/");
    }

}
