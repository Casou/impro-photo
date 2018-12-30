package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.domain.Musique;
import com.bparent.improPhoto.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class MusiqueDto {

    public static final String HANDLER_PLAYLIST = "/handler/playlist/";
    private BigInteger id;
    private String nom;
    private String path;

//    public MusiqueDto(String nom) {
//        this.nom = nom;
//        this.path = FileUtils.formatPathWithCharacter(HANDLER_PLAYLIST + this.nom, "/");
//    }

    public MusiqueDto(Musique musique) {
        this.id = musique.getId();
        this.nom = musique.getName();
        this.path = FileUtils.formatPathWithCharacter(HANDLER_PLAYLIST + musique.getFileName(), "/");
    }

}
