package com.bparent.improPhoto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategorieTypeEnum {

    PHOTO("PHOTO", "Affichage normal"),
    POLAROID("POLAROID", "Affichage polaroïd"),
    TRIPTYQUE("TRIPTYQUE", "Affichage triptyque");

    private String code;
    private String label;
}
