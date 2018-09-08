package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.domain.DateImpro;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.DateImproMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DateImproService {

    @Autowired
    private DateImproDao dateImproDao;

    @Autowired
    private DateImproMapper dateImproMapper;

    public void prepareDates(List<DateImproDto> dates) throws ImproServiceException {
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
