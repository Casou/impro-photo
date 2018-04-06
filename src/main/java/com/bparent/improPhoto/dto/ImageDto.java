package com.bparent.improPhoto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {

    private String nom;
    private String path;
    private String source;

    public ImageDto(String nom, String path) {
        this.nom = nom;
        this.path = path;
        this.source = "/handler/" + path + nom;
    }

}
