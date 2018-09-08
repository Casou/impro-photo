package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.ParametreDao;
import com.bparent.improPhoto.dto.ParametreDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.ParametreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ParametreService {

    @Autowired
    private ParametreDao parametreDao;

    @Autowired
    private ParametreMapper parametreMapper;

    public List<ParametreDto> findAll() throws ImproServiceException {
        try {
            return parametreMapper.toDto(parametreDao.findAll());
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Exception lors du mapping des parmètres");
        }
    }

}
