package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.DateImpro;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.dto.RemerciementDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.mapper.BasicMapper;
import com.bparent.improPhoto.mapper.CategorieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
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



    @Autowired
    private CategorieMapper categorieMapper;

    @Autowired
    private BasicMapper<DateImproDto, DateImpro> dateMapper;

    @Autowired
    private BasicMapper<RemerciementDto, Remerciement> remerciementMapper;



    @RequestMapping("/categories")
    public List<CategorieDto> getAllCategories() throws ImproMappingException {
        return categorieMapper.toDto(this.categorieDao.findAll());
    }

    @RequestMapping("/dates")
    public List<DateImproDto> getAllDates() throws ImproMappingException {
        return dateMapper.toDto(DateImproDto.class, this.dateImproDao.findAll());
    }

    @RequestMapping("/remerciements")
    public List<RemerciementDto> getAllRemerciements() throws ImproMappingException {
        return remerciementMapper.toDto(RemerciementDto.class, this.remerciementDao.findAll());
    }

}
