package com.bparent.improPhoto.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RemerciementsDto {

    private String texte;
    private List<String> photosJoueurs;

}
