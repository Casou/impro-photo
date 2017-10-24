package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    private Boolean booleanField;
    private Integer integerField;
    private Long longField;
    private Double DoubleField;
    private String stringField;
    private Date dateField;
    private BigInteger bigIntegerField;
    private CategorieTypeEnum categorieTypeEnumField;
}