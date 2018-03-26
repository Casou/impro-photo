package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.*;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.mapper.BasicMapper;
import com.bparent.improPhoto.mapper.DateImproMapper;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
public class ListAjaxController {

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private DateImproDao dateImproDao;

    @Autowired
    private RemerciementDao remerciementDao;



    @Autowired
    private DateImproMapper dateMapper;

    @Autowired
    private BasicMapper<RemerciementDto, Remerciement> remerciementMapper;



    @GetMapping("/categories")
    public List<CategorieDto> getAllCategories() throws ImproMappingException {
        return this.categorieService.findAll();
    }

    @RequestMapping("/categoriesWithCompletion")
    public List<CategorieDto> getAllCategoriesWithCompletion() throws ImproMappingException {
        return this.categorieService.findAllWithCompletion();
    }

    @RequestMapping("/dates")
    public List<DateImproDto> getAllDates() throws ImproMappingException {
        return dateMapper.toDto(DateImproDto.class, this.dateImproDao.findAll());
    }

    @RequestMapping("/remerciements")
    public List<RemerciementDto> getAllRemerciements() throws ImproMappingException {
        return remerciementMapper.toDto(RemerciementDto.class, this.remerciementDao.findAll());
    }

    @RequestMapping("/allPhotos")
    public List<String> getAllPhotos() {
        return listPhotos(IConstants.IPath.IPhoto.PHOTOS_INTRODUCTION);
    }

    @RequestMapping("/photosDates")
    public List<String> getPhotosDates() {
        return listPhotos(IConstants.IPath.IPhoto.PHOTOS_PRESENTATION_DATES);
    }

    private List<String> listPhotos(String photosIntroduction) {
        return Arrays.stream(
                new File(photosIntroduction)
                        .listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
        )
                .map(FileUtils::getFrontFilePath) // => /photos
                .collect(Collectors.toList());
    }

    @RequestMapping("/playlistSongs")
    public List<SongDto> getAllPlaylistSongs() {
        return Arrays.stream(
                new File(IConstants.IPath.IAudio.AUDIOS_PLAYLIST)
                        .listFiles((dir, name) -> IConstants.AUDIO_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(file -> file.getName()) // ==> /photos
                .map(fileName -> new SongDto(fileName))
                .collect(Collectors.toList());
    }


    @RequestMapping("/jingles")
    public List<JingleDto> getAllJingles() {
        return Arrays.stream(
                new File(IConstants.IPath.IAudio.AUDIOS_JINGLES)
                        .listFiles((dir, name) -> IConstants.AUDIO_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(file -> file.getName()) // ==> /photos
                .map(fileName -> new JingleDto(fileName))
                .collect(Collectors.toList());
    }

}
