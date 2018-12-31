package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.JingleCategoryDao;
import com.bparent.improPhoto.domain.JingleCategory;
import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.dto.json.ErrorResponse;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.service.JingleService;
import com.bparent.improPhoto.util.CharsetDetector;
import com.bparent.improPhoto.util.FileUtils;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import static com.bparent.improPhoto.controller.MusiqueAjaxController.isAcceptedSongFile;

@RestController
public class JinglesAjaxController {

    @Autowired
    private JingleService jingleService;

    @Autowired
    private JingleCategoryDao jingleCategoryDao;

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

        try {
            importJingleZip(multipart);
        } catch (ImproControllerException e) {
            return new ErrorResponse(e.getMessage());
        }

        return new SuccessResponse("ok");
    }

    private void importJingleZip(MultipartFile multipart)
            throws IOException, ImproControllerException {
        final UploadedFileDto uploadedFileDto = ZipUtils.copySingleUploadedFile(multipart, multipart.getOriginalFilename(), IConstants.IPath.MEDIAS_TEMP);
        final File tempZipFile = new File(IConstants.IPath.MEDIAS_TEMP + uploadedFileDto.getFileName());
        final ZipFile zipFile = new ZipFile(tempZipFile, CharsetDetector.detectCharset(tempZipFile));

        List<UploadedFileDto> zipFilesList = ZipUtils.listZipFiles(zipFile, IConstants.AUDIO_EXTENSION_ACCEPTED, false);

        if (zipFilesList.stream().anyMatch(UploadedFileDto::getIsFile)
                || zipFilesList.stream().flatMap(dto -> dto.getChildren().stream()).anyMatch(dto -> dto.getChildren().size() > 0)) {
            throw new ImproControllerException("Le fichier .zip n'est pas bien formé. Regardez l'image d'aide pour savoir comment créer votre archive.");
        }

        List<UploadedFileDto> extractedFiles = extractZipFiles(zipFile, zipFilesList);

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
            throw new ImproControllerException("Une erreur s'est produite lors de l'import des fichiers suivants : " + nonUploadedFiles.stream()
                    .map(UploadedFileDto::getName)
                    .collect(Collectors.joining(", ")));
        }

        if (!tempZipFile.delete()) {
            throw new ImproControllerException("Les fichiers ont bien été importés mais le dossier temporaire n'a pas pu être vidé.");
        }
    }

    private List<UploadedFileDto> extractZipFiles(ZipFile zipFile, List<UploadedFileDto> zipFiles) {
        return zipFiles.stream().map(dto -> {
            UploadedFileDto uploadedFileDto = ZipUtils.extractZipFile(zipFile, dto, IConstants.IPath.IAudio.AUDIOS_JINGLES);
            uploadedFileDto.setChildren(extractZipFiles(zipFile, uploadedFileDto.getChildren()));
            return uploadedFileDto;
        }).collect(Collectors.toList());
    }

    @PostMapping(value = "/jingleCategory/{categoryId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadJinglesIntoCategory(@PathVariable BigInteger categoryId,
                                                                                   MultipartHttpServletRequest request,
                                                                                   HttpServletResponse response) throws IOException {
        JingleCategory category = jingleCategoryDao.findOne(categoryId);
        if (category == null) {
            return new ErrorResponse("Category [" + categoryId + "] does not exists.");
        }

        List<String> errorMessages = new ArrayList<>();
        for (MultipartFile multipart : request.getFiles("file")) {
            final String fileName = multipart.getOriginalFilename();
            final String fileExtension = FileUtils.getFileExtension(fileName);
            if (IConstants.ZIP_EXTENSION.equals(fileExtension)) {
                try {
                    importFlatJingleZip(multipart, category);
                } catch (ImproControllerException e) {
                    errorMessages.add(e.getMessage());
                }
            } else {
                if (!isAcceptedSongFile.test(fileExtension)) {
                    errorMessages.add("Erreur lors de l'import du fichier " + fileName + " : ce type de fichier n'est pas autorisé.");
                    continue;
                }

                try {
                    UploadedFileDto uploadedFile = ZipUtils.copySingleUploadedFile(multipart, fileName,
                            IConstants.IPath.IAudio.AUDIOS_JINGLES + category.getFolderName() + "/");

                    jingleService.saveUploadedJingle(uploadedFile, category);
                } catch(RejectedExecutionException e) {
                    errorMessages.add("Erreur lors de l'import du fichier " + fileName + " : erreur lors de la copie");
                }
            }
        }

        if (errorMessages.size() > 0) {
            return new ErrorResponse(errorMessages.stream().collect(Collectors.joining("\n")));
        }

        return new SuccessResponse("ok");
    }

    private void importFlatJingleZip(MultipartFile multipart, JingleCategory category)
            throws IOException, ImproControllerException {
        final UploadedFileDto uploadedFileDto = ZipUtils.copySingleUploadedFile(multipart, multipart.getOriginalFilename(), IConstants.IPath.MEDIAS_TEMP);
        final File tempZipFile = new File(IConstants.IPath.MEDIAS_TEMP + uploadedFileDto.getFileName());
        final ZipFile zipFile = new ZipFile(tempZipFile, CharsetDetector.detectCharset(tempZipFile));

        List<UploadedFileDto> zipFilesList = ZipUtils.listZipFiles(zipFile, IConstants.AUDIO_EXTENSION_ACCEPTED, false);

        if (zipFilesList.stream().anyMatch(
                file -> file.getChildren().size() > 0)) {
            throw new ImproControllerException("Le fichier .zip n'est pas bien formé. Il ne doit pas contenir de dossier.");
        }

        UploadedFileDto categoryDto = UploadedFileDto.builder()
                .fileName(category.getFolderName())
                .name(category.getName())
                .isFile(false)
                .build();
        zipFilesList.forEach(f -> f.setParent(categoryDto));

        List<UploadedFileDto> extractedFiles = extractZipFiles(zipFile, zipFilesList);

        zipFile.close();

        List<UploadedFileDto> uploadedFiles = extractedFiles.stream()
                .filter(UploadedFileDto::getUploadSuccess)
                .collect(Collectors.toList());
        uploadedFiles.forEach(file -> jingleService.saveUploadedJingle(file, category));

        List<UploadedFileDto> nonUploadedFiles = extractedFiles.stream()
                .filter(dto -> !dto.getUploadSuccess())
                .collect(Collectors.toList());
        if (nonUploadedFiles.size() > 0) {
            tempZipFile.delete();
            throw new ImproControllerException("Une erreur s'est produite lors de l'import des fichiers suivants : " + nonUploadedFiles.stream()
                    .map(UploadedFileDto::getName)
                    .collect(Collectors.joining(", ")));
        }

        if (!tempZipFile.delete()) {
            throw new ImproControllerException("Les fichiers ont bien été importés mais le dossier temporaire n'a pas pu être vidé.");
        }
    }

    @DeleteMapping(value = "/jingle/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteJingle(@PathVariable BigInteger id) {
        jingleService.deleteJingle(id);
        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/jingleCategory/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteJingleCategory(@PathVariable BigInteger id) {
        this.jingleService.deleteJingleCategory(id);

        return new SuccessResponse("ok");
    }

}
