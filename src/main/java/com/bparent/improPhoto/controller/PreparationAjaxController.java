package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.ImageDto;
import com.bparent.improPhoto.dto.form.PreparationForm;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.service.DateImproService;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import com.bparent.improPhoto.util.ZipUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;

@RestController
public class PreparationAjaxController {

    @Autowired
    private DateImproService dateImproService;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private EtatImproService etatImproService;

    private static final Predicate<String> isAcceptedPictureFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension)
            || IConstants.PICTURE_EXTENSION_ACCEPTED.contains(fileExtension);


    @PostMapping(value = "/preparation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> savePreparation(@RequestBody PreparationForm form) throws ImproControllerException {

        if (form.getDatesImpro() != null) {
            try {
                dateImproService.prepareDates(form.getDatesImpro());
            } catch (ImproServiceException e) {
                throw new ImproControllerException("Error while saving dates in preparation impro", e);
            }
        }

        if (form.getCategories() != null) {
            try {
                categorieService.prepareCategories(form.getCategories());
            } catch (ImproServiceException e) {
                throw new ImproControllerException("Error while saving categories in preparation impro", e);
            }
        }

        return new SuccessResponse("ok");
    }

    @PutMapping("/preparation/resetImpro")
    public @ResponseBody ResponseEntity<MessageResponse> resetImpro() {
        etatImproService.resetImpro();
        return new SuccessResponse("ok");
    }


    @PostMapping(value = "/preparation/video/presentateur", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadVideoPresentateur(MultipartHttpServletRequest request) {
        copyVideoFile(request, IConstants.IPath.IVideo.VIDEO_NAME_PRESENTATEUR);

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/preparation/video/joueurs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadVideoJoueur(MultipartHttpServletRequest request) {
        copyVideoFile(request, IConstants.IPath.IVideo.VIDEO_NAME_JOUEURS);

        return new SuccessResponse("ok");
    }

    private void copyVideoFile(MultipartHttpServletRequest request, String videoName) {
        List<MultipartFile> uploadedFiles = request.getFiles("file");

        if (uploadedFiles.isEmpty()) {
            throw new IllegalArgumentException("You have to upload a file");
        } else if (uploadedFiles.size() > 1) {
            throw new IllegalArgumentException("Too many files uploaded");
        }

        MultipartFile multipartFile = uploadedFiles.get(0);
        if (StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new IllegalArgumentException("You have to upload a file");
        }
        ZipUtils.copySingleUploadedFile(multipartFile, videoName, IConstants.IPath.IVideo.VIDEO_INTRO);
    }

    @DeleteMapping(value = "/preparation/images", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteImage(@RequestBody ImageDto imageDto) {
        File image = new File(imageDto.getPath() + imageDto.getNom());
        if (!image.delete()) {
            throw new RejectedExecutionException("Error while deleting file " + image.getAbsolutePath());
        }

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/preparation/images/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteAllImage(@RequestBody ImageDto imageDto) {
        String folderPath;
        switch (imageDto.getNom()) {
            case "intro" :
                folderPath = IConstants.IPath.IPhoto.PHOTOS_INTRODUCTION;
                break;
            case "dates" :
                folderPath = IConstants.IPath.IPhoto.PHOTOS_PRESENTATION_DATES;
                break;
            case "joueurs" :
                folderPath = IConstants.IPath.IPhoto.PHOTOS_JOUEURS;
                break;
            default :
                throw new IllegalArgumentException("Folder not known " + imageDto.getNom());
        }

        File folder = new File(folderPath);
        for (File file : folder.listFiles((dir, name) ->
                IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))) {
            if (!file.delete()) {
                throw new RejectedExecutionException("Error while deleting file " + file.getAbsolutePath());
            }
        }

        return new SuccessResponse("ok");
    }


    @PostMapping(value = "/preparation/images/intro", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadImagesIntro(MultipartHttpServletRequest request) {
        handleUploadImages(request, IConstants.IPath.IPhoto.PHOTOS_INTRODUCTION);

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/preparation/images/dates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadImagesDates(MultipartHttpServletRequest request) {
        handleUploadImages(request, IConstants.IPath.IPhoto.PHOTOS_PRESENTATION_DATES);

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/preparation/images/joueurs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadImagesJoueurs(MultipartHttpServletRequest request) {
        FileUtils.deleteFolderContent(new File(IConstants.IPath.IPhoto.PHOTOS_JOUEURS));
        handleUploadImages(request, IConstants.IPath.IPhoto.PHOTOS_JOUEURS);

        return new SuccessResponse("ok");
    }

    private void handleUploadImages(MultipartHttpServletRequest request, String folderPath) {
        List<MultipartFile> uploadedFiles = request.getFiles("file");

        if (uploadedFiles.isEmpty()) {
            throw new IllegalArgumentException("You have to upload a file");
        }

        uploadedFiles.forEach(multipartFile -> {
            if (StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
                return;
            }
            ZipUtils.copyAnyUploadedFile(multipartFile, isAcceptedPictureFile,
                    IConstants.PICTURE_EXTENSION_ACCEPTED, folderPath, true);
        });
    }

}
