package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.mapper.CategorieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CategorieService {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private CategorieMapper categorieMapper;

    public List<CategorieDto> findAll() throws ImproMappingException {
        return categorieMapper.toDto(categorieDao.findAll());
    }

    public CategorieDto findById(BigInteger id) throws ImproMappingException {
        return categorieMapper.toDto(this.categorieDao.findById(id));
    }

    public void save(CategorieDto dto) throws ImproMappingException {
        this.categorieDao.save(categorieMapper.toEntity(dto));
    }
}
