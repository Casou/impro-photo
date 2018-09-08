package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    private List<ImageDto> pictures = new ArrayList<>();
    private Boolean tooManyPictures = Boolean.FALSE;

    public boolean isPersistable() {
        return !StringUtils.isEmpty(this.nom) && this.type != null;
    }

}
