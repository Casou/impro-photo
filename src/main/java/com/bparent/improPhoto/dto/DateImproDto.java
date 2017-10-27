package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.util.DateFrDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateImproDto {

    private BigInteger id;
    @JsonDeserialize(using=DateFrDeserializer.class)
    private Date date;
    private String nom;

}
