package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.form.PreparationForm;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.service.PreparationService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Predicate;

@RestController
public class PreparationAjaxController {

    @Autowired
    private PreparationService preparationService;

    @Autowired
    private EtatImproService etatImproService;

    private static final Predicate<String> isAcceptedVideoFile = IConstants.VIDEO_EXTENSION_ACCEPTED::contains;


    @PostMapping(value = "/preparation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> savePreparation(@RequestBody PreparationForm form) throws ImproControllerException {

        try {
            preparationService.prepareImpro(form.getCategories(), form.getRemerciements(), form.getDatesImpro());
        } catch (ImproServiceException e) {
            throw new ImproControllerException("Error while saving preparation impro", e);
        }

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
        FileUtils.copyUploadedFile(multipartFile, videoName, IConstants.IPath.IVideo.VIDEO_INTRO);
    }

}
