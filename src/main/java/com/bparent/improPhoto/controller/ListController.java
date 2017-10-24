package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.domain.DateImpro;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private DateImproDao dateImproDao;

    @Autowired
    private RemerciementDao remerciementDao;

    @RequestMapping("/categories")
    public List<Categorie> getAllCategories() {
        return this.categorieDao.findAll();
    }

    @RequestMapping("/categories/types")
    public List<BasicCodeLabelDto> getAllCategorieTypes() {
        return Arrays.stream(CategorieTypeEnum.values())
                .map(type -> new BasicCodeLabelDto(type.getCode(), type.getLabel()))
                .collect(Collectors.toList());
    }

    @RequestMapping("/dates")
    public List<DateImpro> getAllDates() {
        return this.dateImproDao.findAll();
    }

    @RequestMapping("/remerciements")
    public List<Remerciement> getAllRemerciements() {
        return this.remerciementDao.findAll();
    }

}
