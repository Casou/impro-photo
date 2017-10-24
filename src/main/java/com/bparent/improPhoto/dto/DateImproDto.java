package com.bparent.improPhoto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
public class DateImproDto {

    private BigInteger id;
    private Date date;
    private String nom;

}
