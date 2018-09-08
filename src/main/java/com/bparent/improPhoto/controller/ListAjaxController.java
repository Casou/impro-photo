package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.ParametreDao;
import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.dto.*;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.DateImproMapper;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.service.JingleService;
import com.bparent.improPhoto.service.RemerciementService;
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
import java.util.stream.Stream;

@RestController
@RequestMapping("/list")
public class ListAjaxController {

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private DateImproDao dateImproDao;

    @Autowired
    private RemerciementService remerciementService;

    @Autowired
    private DateImproMapper dateMapper;

    @Autowired
    private ParametreDao parametreDao;

    @Autowired
    private JingleService jingleService;



    @GetMapping("/parametres")
    public List<Parametre> getAllParametres() {
        return this.parametreDao.findAll();
    }


    @GetMapping("/categories")
    public List<CategorieDto> getAllCategories() throws ImproMappingException {
        return this.categorieService.findAll();
    }

    @GetMapping("/categoriesWithCompletion")
    public List<CategorieDto> getAllCategoriesWithCompletion() throws ImproMappingException {
        return this.categorieService.findAllWithCompletion();
    }

    @GetMapping("/dates")
    public List<DateImproDto> getAllDates() throws ImproMappingException {
        return dateMapper.toDto(DateImproDto.class, this.dateImproDao.findAll());
    }

    @GetMapping("/remerciements")
    public RemerciementsDto getAllRemerciements() throws ImproServiceException {
        return RemerciementsDto.builder().texte(remerciementService.getRemerciements()).build();
    }

    @GetMapping("/allPhotos")
    public List<String> getAllPhotos() {
        return listPhotos(IConstants.IPath.IPhoto.PHOTOS_INTRODUCTION);
    }

    @GetMapping("/photosDates")
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

    @GetMapping("/playlistSongs")
    public List<SongDto> getAllPlaylistSongs() {
        return Arrays.stream(
                new File(IConstants.IPath.IAudio.AUDIOS_PLAYLIST)
                        .listFiles((dir, name) -> IConstants.AUDIO_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(file -> file.getName()) // ==> /photos
                .map(fileName -> new SongDto(fileName))
                .collect(Collectors.toList());
    }


    @GetMapping("/jingles")
    public List<JingleCategoryDto> getAllJingles() {
        return jingleService.getAllJingles();
    }


    @GetMapping("/pictures/intro")
    public List<ImageDto> getPicturesIntro() {
        return listPictures(IConstants.IPath.IPhoto.PHOTOS_INTRODUCTION);
    }
    @GetMapping("/pictures/dates")
    public List<ImageDto> getPicturesDates() {
        return listPictures(IConstants.IPath.IPhoto.PHOTOS_PRESENTATION_DATES);
    }
    @GetMapping("/pictures/joueurs")
    public List<ImageDto> getPicturesJoueurs() {
        return listPictures(IConstants.IPath.IPhoto.PHOTOS_JOUEURS);
    }

    private List<ImageDto> listPictures(String pathFolder) {
        return Arrays.stream(
                new File(pathFolder)
                        .listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(file -> file.getName())
                .map(fileName -> new ImageDto(fileName, pathFolder))
                .collect(Collectors.toList());
    }


    @GetMapping("/allImages")
    public List<ImageDto> getAllPictures() {
        List<ImageDto> imagesCategories = Arrays.stream(new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO).listFiles((dir, name) -> dir.isDirectory()))
                .map(folderCategory -> folderCategory.listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase()))))
                .flatMap(files -> Arrays.asList(files).stream())
                .map(file -> new ImageDto(file.getName(), IConstants.IPath.IPhoto.PHOTOS_IMPRO + file.getParentFile().getName() + "/"))
                .collect(Collectors.toList());

        return Stream.of(
                imagesCategories.stream(),
                listPictures(IConstants.IPath.IPhoto.PHOTOS_INTRODUCTION).stream(),
                listPictures(IConstants.IPath.IPhoto.PHOTOS_PRESENTATION_DATES).stream(),
                listPictures(IConstants.IPath.IPhoto.PHOTOS_JOUEURS).stream()
                )
                .reduce(Stream::concat)
                .orElse(Stream.empty())
                .collect(Collectors.toList());
    }

}
