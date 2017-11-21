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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        categorieDao.deleteByIdNotIn(categories.stream()
                .filter(categorie -> categorie.getId() != null)
                .map(categorie -> categorie.getId())
                .collect(Collectors.toList()));
        try {
            categories.forEach(cat -> cat.setTermine(false));
            categorieDao.save(categorieMapper.toEntity(categories));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while saving categorie_list", e);
        }

        remerciementDao.deleteByIdNotIn(Arrays.asList(remerciement.getId()));
        try {
            remerciementDao.save(remerciementMapper.toEntity(Remerciement.class, remerciement));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while saving remerciements", e);
        }

        dateImproDao.deleteByIdNotIn(dates.stream()
                .filter(categorie -> categorie.getId() != null)
                .map(date -> date.getId())
                .collect(Collectors.toList()));
        try {
            dateImproDao.save(dateImproMapper.toEntity(DateImpro.class, dates));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while saving dates", e);
        }
    }

}
