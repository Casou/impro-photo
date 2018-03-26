package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorieDto {

    private BigInteger id;
    private String nom;
    private CategorieTypeEnum type;
    private String pathFolder;
    private Boolean termine;
    private Integer ordre;

    private Boolean existsInDatabase = Boolean.TRUE;
    private Boolean pathInError = Boolean.TRUE;
    private Integer nbPictures;
    private Boolean tooManyPictures;

    public boolean isPersistable() {
        return !StringUtils.isEmpty(this.nom) && this.type != null;
    }

}
