package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.CategorieMapper;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private CategorieMapper categorieMapper;

    public List<CategorieDto> findAll() throws ImproMappingException {
        return categorieMapper.toDto(categorieDao.findAll());
    }

    public List<CategorieDto> findAllWithCompletion() throws ImproMappingException {
        List<CategorieDto> databaseCategories = categorieMapper.toDto(categorieDao.findAll());
        databaseCategories.addAll(this.getMissingCategories(databaseCategories));

        databaseCategories.forEach(categorieDto -> {
            File folder = new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorieDto.getPathFolder());
            if (folder.exists()) {
                categorieDto.setNbPictures(folder.listFiles().length);
                categorieDto.setTooManyPictures(categorieDto.getNbPictures() > IConstants.LIMIT_PICTURES);
            }
        });

        return databaseCategories;
    }

    public List<CategorieDto> getMissingCategoriesFromDatabase(List<Categorie> presentCategories) throws ImproServiceException {
        try {
            return this.getMissingCategories(categorieMapper.toDto(presentCategories));
        } catch (ImproMappingException e) {
            throw new ImproServiceException("Error while searching missing categories from database (mapping exception)", e);
        }
    }

    public List<CategorieDto> getMissingCategories(List<CategorieDto> presentCategories) {
        List<CategorieDto> missingCategories = new ArrayList<>();
        List<File> systemCategorieFolders = Arrays.asList(new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO).listFiles())
                .stream()
                .filter(file -> file.isDirectory())
                .collect(Collectors.toList());

        systemCategorieFolders.forEach(categorieFolder -> {
            Optional<CategorieDto> categorieFound = presentCategories.stream()
                    .filter(dbCategorie -> dbCategorie.getPathFolder().equals(categorieFolder.getName()))
                    .findFirst();
            if (!categorieFound.isPresent()) {
                missingCategories.add(CategorieDto.builder()
                        .ordre(9999)
                        .type(CategorieTypeEnum.PHOTO)
                        .pathFolder(categorieFolder.getName())
                        .pathInError(false)
                        .existsInDatabase(false)
                        .build());
            }
        });

        return missingCategories;
    }

    public CategorieDto findById(BigInteger id) throws ImproMappingException {
        return categorieMapper.toDto(this.categorieDao.findById(id));
    }

    public void save(CategorieDto dto) throws ImproMappingException {
        this.categorieDao.save(categorieMapper.toEntity(dto));
    }
}
