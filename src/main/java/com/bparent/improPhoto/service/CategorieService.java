package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.ImageDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.CategorieMapper;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.math.BigInteger;
import java.util.*;
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
                categorieDto.setPictures(
                        Arrays.stream(folder.listFiles())
                                .map(file -> new ImageDto(file.getName(), IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorieDto.getPathFolder() + "/"))
                                .collect(Collectors.toList()));
                categorieDto.setTooManyPictures(categorieDto.getPictures().size() > IConstants.LIMIT_PICTURES);
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
                        .nom(FileUtils.capitalizeCategoryFolderName(categorieFolder.getName()))
                        .pathFolder(categorieFolder.getName())
                        .pathInError(false)
                        .existsInDatabase(false)
                        .build());
            }
        });

        missingCategories.sort(Comparator.comparing(CategorieDto::getPathFolder));

        return missingCategories;
    }

    public CategorieDto findById(BigInteger id) throws ImproMappingException {
        return categorieMapper.toDto(this.categorieDao.findById(id));
    }

    public void save(CategorieDto dto) throws ImproMappingException {
        this.categorieDao.save(categorieMapper.toEntity(Categorie.class, dto));
    }

    public void deleteAllCategories() {
        categorieDao.findAll().forEach(categorie -> {
            categorieDao.delete(categorie);
            FileUtils.deleteFolder(new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorie.getPathFolder()));
        });
    }

    @Transactional
    public void prepareCategories(List<CategorieDto> categories) throws ImproServiceException {
        List<CategorieDto> categoriesToSave = categories.stream()
                .filter(CategorieDto::isPersistable)
                .collect(Collectors.toList());
        if (categoriesToSave.isEmpty()) {
            categorieDao.deleteAll();
        } else {
            categorieDao.deleteByIdNotIn(categoriesToSave.stream()
                    .filter(categorie -> categorie.getId() != null)
                    .map(categorie -> categorie.getId())
                    .collect(Collectors.toList()));
            try {
                categoriesToSave.forEach(cat -> cat.setTermine(false));
                categorieDao.save(categorieMapper.toEntity(Categorie.class, categoriesToSave));
            } catch (ImproMappingException e) {
                throw new ImproServiceException("Error while saving categorie_list", e);
            }
        }

        List<CategorieDto> categoriesToDelete = this.getMissingCategories(categories);
        categoriesToDelete.forEach(categorieDto -> FileUtils.deleteFolder(new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorieDto.getPathFolder())));
    }

    public void saveUploadedCategories(List<UploadedFileDto> uploadedFiles) {
        List<Categorie> categories = uploadedFiles.stream().map(uploadedFileDto -> {
            Categorie categorie = Categorie.builder()
                    .nom(uploadedFileDto.getName())
                    .pathFolder(uploadedFileDto.getFileName())
                    .termine(false)
                    .type(CategorieTypeEnum.PHOTO)
                    .ordre(1)
                    .build();
            return categorie;
        }).collect(Collectors.toList());

        categories.sort(Comparator.comparing(Categorie::getNom));
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).setOrdre(i);
        }

        categorieDao.save(categories);
    }
}
