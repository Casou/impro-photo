package com.bparent.improPhoto.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JingleCategoryDto {

    private String nom;
    private List<JingleDto> jingles;

}
