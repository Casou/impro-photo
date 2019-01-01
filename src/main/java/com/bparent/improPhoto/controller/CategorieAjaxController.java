package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.dto.json.ErrorResponse;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

@RestController
@Slf4j
public class CategorieAjaxController {

    private static final Predicate<String> isAcceptedSongFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension);

    @Autowired
    private CategorieService categorieService;

    @GetMapping("/categories")
    public List<CategorieDto> getAllCategories() throws ImproMappingException {
        return this.categorieService.findAll();
    }

    @GetMapping("/categories/getPictures")
    public List<String> getPictures(final String pathFolder) {
        return Arrays.stream(
                new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + pathFolder)
                        .listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(FileUtils::getFrontFilePath) // ==> /photos
                .limit(IConstants.LIMIT_PICTURES)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ResponseEntity<MessageResponse> uploadCategories(MultipartHttpServletRequest request) throws IOException {
        List<MultipartFile> uniqueZipFileList = request.getFiles("file").stream().filter(multipartFile -> !StringUtils.isEmpty(multipartFile.getOriginalFilename()))
                .collect(Collectors.toList());

        if (uniqueZipFileList.size() != 1) {
            return new ErrorResponse("L'import des catégories a besoin d'un (et un seul) fichier .zip en entrée. "
                    + uniqueZipFileList.size() + " fichier(s) envoyé(s) dans la requête");
        }

        try {
            importCategories(uniqueZipFileList.get(0));
        } catch (ImproControllerException e) {
            return new ErrorResponse(e.getMessage());
        }

        return new SuccessResponse("ok");
    }

    private void importCategories(MultipartFile multipart) throws IOException, ImproControllerException {

        final UploadedFileDto uploadedFileDto = ZipUtils.copySingleUploadedFile(multipart, multipart.getOriginalFilename(), IConstants.IPath.MEDIAS_TEMP);
        final File tempZipFile = new File(IConstants.IPath.MEDIAS_TEMP + uploadedFileDto.getFileName());
        final ZipFile zipFile = new ZipFile(tempZipFile, CharsetDetector.detectCharset(tempZipFile));

        List<UploadedFileDto> zipFilesList = ZipUtils.listZipFiles(zipFile, IConstants.PICTURE_EXTENSION_ACCEPTED, false);

        if (zipFilesList.stream().anyMatch(UploadedFileDto::getIsFile)
                || zipFilesList.stream().flatMap(dto -> dto.getChildren().stream()).anyMatch(dto -> dto.getChildren().size() > 0)) {
            throw new ImproControllerException("Le fichier .zip n'est pas bien formé. Regardez l'image d'aide pour savoir comment créer votre archive.");
        }

        List<UploadedFileDto> extractedFiles = JinglesAjaxController.extractZipFiles(zipFile, zipFilesList, IConstants.IPath.IPhoto.PHOTOS_IMPRO);

        zipFile.close();

        List<UploadedFileDto> uploadedFiles = extractedFiles.stream()
                .filter(UploadedFileDto::getUploadSuccess)
                .collect(Collectors.toList());
        categorieService.saveUploadedCategories(uploadedFiles);

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


    @DeleteMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteCategories() {
        categorieService.deleteAllCategories();
        return new SuccessResponse("ok");
    }

}
