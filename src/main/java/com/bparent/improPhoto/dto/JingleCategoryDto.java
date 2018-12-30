package com.bparent.improPhoto.dto;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JingleCategoryDto {

    private BigInteger id;
    private String name;
    private List<JingleDto> jingles;

}
