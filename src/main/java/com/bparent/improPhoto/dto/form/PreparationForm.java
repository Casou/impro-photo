package com.bparent.improPhoto.dto.form;

import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.DateImproDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PreparationForm {

    private List<CategorieDto> categories;
    private List<DateImproDto> datesImpro;

}
