package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.DateImpro;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.dto.RemerciementDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.CategorieMapper;
import com.bparent.improPhoto.mapper.DateImproMapper;
import com.bparent.improPhoto.mapper.RemerciementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PreparationService {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private CategorieMapper categorieMapper;

    @Autowired
    private RemerciementDao remerciementDao;

    @Autowired
    private RemerciementMapper remerciementMapper;

    @Autowired
    private DateImproDao dateImproDao;

    @Autowired
    private DateImproMapper dateImproMapper;

    public void prepareImpro(List<CategorieDto> categories, RemerciementDto remerciement, List<DateImproDto> dates) throws ImproServiceException {
        categorieDao.deleteAll();
        try {
            categories.forEach(cat -> cat.setTermine(false));
            categorieDao.save(categorieMapper.toEntity(categories));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while saving categories", e);
        }

        remerciementDao.deleteAll();
        try {
            remerciementDao.save(remerciementMapper.toEntity(Remerciement.class, remerciement));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while saving remerciements", e);
        }

        dateImproDao.deleteAll();
        try {
            dateImproDao.save(dateImproMapper.toEntity(DateImpro.class, dates));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while saving dates", e);
        }
    }

}
