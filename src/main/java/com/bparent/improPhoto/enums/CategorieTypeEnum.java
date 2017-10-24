package com.bparent.improPhoto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategorieTypeEnum {

    PHOTO("PHOTO", "Affichage normal"),
    POLAROID("POLAROID", "Affichage polaroïd"),
    TRYPTIQUE("TRYPTIQUE", "Affichage tryptique");

    private String code;
    private String label;
}
