package com.bparent.improPhoto.dto;

import lombok.Data;

import java.util.List;

@Data
public class EtatImproDto {

    private String ecran;
    private Integer idEcran;
    private String typeEcran;
    private List<Integer> photosChoisies;
    private Boolean integralite;
    private Integer photoCourante;
    private String statutDiapo;
    private List<Integer> blockMasques;

}
