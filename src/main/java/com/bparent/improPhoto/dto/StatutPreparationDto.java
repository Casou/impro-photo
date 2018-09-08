package com.bparent.improPhoto.dto;

import lombok.Data;

@Data
public class StatutPreparationDto {

    private Boolean improLaunched;
    private Boolean categories;
    private Boolean videoPresentationPresentateur;
    private Boolean videoPresentationJoueurs;
    private String remerciements;
    private Integer photosJoueurs;
    private Integer photosPresentationDates;

}
