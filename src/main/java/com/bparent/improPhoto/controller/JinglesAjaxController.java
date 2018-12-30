package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.dto.JingleDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.dto.json.ErrorResponse;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.service.JingleService;
import com.bparent.improPhoto.util.CharsetDetector;
import com.bparent.improPhoto.util.IConstants;
import com.bparent.improPhoto.util.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import static com.bparent.improPhoto.controller.MusiqueAjaxController.deleteSong;
import static com.bparent.improPhoto.controller.MusiqueAjaxController.isAcceptedSongFile;

@RestController
public class JinglesAjaxController {

    @Autowired
    private JingleService jingleService;

    @GetMapping("/jingles")
    public List<JingleCategoryDto> getAllJingles() {
        return jingleService.getAllJingles();
    }

    @PostMapping(value = "/jingles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadJingles(MultipartHttpServletRequest request,
                                                                       HttpServletResponse response) throws IOException {

        if (request.getFiles("file").size() != 1) {
            return new ErrorResponse("L'import de jingle a besoin d'un (et un seul) fichier .zip en entrée. "
                    + request.getFiles("file").size() + " fichier(s) envoyé(s) dans la requête");
        }

        MultipartFile multipart = request.getFiles("file").get(0);

        final UploadedFileDto uploadedFileDto = ZipUtils.copySingleUploadedFile(multipart, multipart.getOriginalFilename(), IConstants.IPath.MEDIAS_TEMP);
        final File tempZipFile = uploadedFileDto.getDestinationFile();
        final ZipFile zipFile = new ZipFile(tempZipFile, CharsetDetector.detectCharset(tempZipFile));

        List<UploadedFileDto> zipFiles = ZipUtils.listZipFiles(zipFile, IConstants.AUDIO_EXTENSION_ACCEPTED,
                IConstants.IPath.IAudio.AUDIOS_JINGLES, false);

        if (!checkJingleZipComposition(zipFiles)) {
            return new ErrorResponse("Le fichier .zip n'est pas bien formé. Regardez l'image d'aide pour savoir comment créer votre archive.");
        }

        List<UploadedFileDto> extractedFiles = zipFiles.stream()
                .flatMap(UploadedFileDto::streamChildren)
                .peek(dto -> dto.setUploadSuccess(ZipUtils.extractZipFile(zipFile, dto)))
                .collect(Collectors.toList());

        zipFile.close();

        List<UploadedFileDto> uploadedFiles = extractedFiles.stream()
                .filter(dto -> dto.getChildren().size() > 0)
                .filter(UploadedFileDto::getUploadSuccess)
                .collect(Collectors.toList());
        jingleService.saveUploadedJingles(uploadedFiles);

        List<UploadedFileDto> nonUploadedFiles = extractedFiles.stream()
                .filter(dto -> !dto.getUploadSuccess())
                .collect(Collectors.toList());
        if (nonUploadedFiles.size() > 0) {
            tempZipFile.delete();
            return new ErrorResponse("Une erreur s'est produite lors de l'import des fichiers suivants : " + nonUploadedFiles.stream()
                    .map(UploadedFileDto::getName)
                    .collect(Collectors.joining(", ")));
        }

        if (!tempZipFile.delete()) {
            return new ErrorResponse("Les fichiers ont bien été importés mais le dossier temporaire n'a pas pu être vidé.");
        }

        return new SuccessResponse("ok");
    }

    private boolean checkJingleZipComposition(List<UploadedFileDto> zipFiles) {
        if (zipFiles.stream().anyMatch(UploadedFileDto::getIsFile)) {
            return false;
        }
        return !zipFiles.stream().flatMap(dto -> dto.getChildren().stream()).anyMatch(dto -> dto.getChildren().size() > 0);
    }

    @PostMapping(value = "/jingleCategory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadJinglesIntoCategory(MultipartHttpServletRequest request,
                                                                                   HttpServletResponse response) {
        List<UploadedFileDto> uploadedFiles = request.getFiles("file").stream()
                .map(multipart -> ZipUtils.copyAnyUploadedFile(multipart, isAcceptedSongFile, IConstants.AUDIO_EXTENSION_ACCEPTED,
                        IConstants.IPath.IAudio.AUDIOS_JINGLES + request.getParameter("category_name") + "/", true))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/jingle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteJingle(@RequestBody JingleDto jingleDto) {
        deleteSong(IConstants.IPath.IAudio.AUDIOS_JINGLES + jingleDto.getFolder() + "/", jingleDto.getNom());

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/jingleCategory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteJingleCategory(@RequestBody JingleCategoryDto jingleCategoryDto) {
        this.jingleService.deleteJingleCategory(jingleCategoryDto);

        return new SuccessResponse("ok");
    }

}
