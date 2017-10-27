package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDto {

    private BigInteger id;
    private String nom;
    private CategorieTypeEnum type;
    private String pathFolder;
    private Boolean termine;
    private Integer ordre;

    private Boolean pathInError = Boolean.TRUE;

}
