package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.util.ScreensEnum;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
public class EtatImproDto {

    private String ecran = ScreensEnum.SALLE_ATTENTE.getCode();
    private Integer idCategorie = -1;
    private String typeEcran = CategorieTypeEnum.PHOTO.getCode();
    private List<BigInteger> photosChoisies = new ArrayList<>();
    private Boolean integralite = false;
    private Integer photoCourante = -1;
    private String statutDiapo;
    private List<BigInteger> blockMasques = new ArrayList<>();
    private MusiqueDto currentSong;
    private Boolean isPlaylistPlaying;
    private Integer playlistVolume = 10;
    private Boolean categoriesShown = false;

}
