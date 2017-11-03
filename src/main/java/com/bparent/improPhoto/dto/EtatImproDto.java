package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.util.ScreensEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EtatImproDto {

    private String ecran = ScreensEnum.SALLE_ATTENTE.getCode();
    private Integer idEcran = -1;
    private String typeEcran = CategorieTypeEnum.PHOTO.getCode();
    private List<Integer> photosChoisies = new ArrayList<>();
    private Boolean integralite = false;
    private Integer photoCourante = -1;
    private String statutDiapo;
    private List<Integer> blockMasques = new ArrayList<>();

}
