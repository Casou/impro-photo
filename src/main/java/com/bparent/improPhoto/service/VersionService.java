package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.VersionDao;
import com.bparent.improPhoto.dto.VersionDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.VersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VersionService {

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private VersionMapper versionMapper;

    public List<VersionDto> findAll() {
        return versionMapper.toDto(VersionDto.class, versionDao.findAll());
    }

}
