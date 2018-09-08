package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class CategorieAjaxController {

    private static final Predicate<String> isAcceptedSongFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension);

    @Autowired
    private CategorieService categorieService;

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
    ResponseEntity<MessageResponse> uploadCategories(MultipartHttpServletRequest request) {
        request.getFiles("file").forEach(multipart -> FileUtils.handleUploadedFile(multipart, isAcceptedSongFile,
                IConstants.PICTURE_EXTENSION_ACCEPTED, IConstants.IPath.IPhoto.PHOTOS_IMPRO, false));

        // Clean directory
        File folder = new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO);
        List<File> files = Arrays.asList(folder.listFiles((file) -> file.isFile()));
        files.forEach(File::delete);

        return new SuccessResponse("ok");
    }


    @DeleteMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteCategories() {
        categorieService.deleteAllCategories();
        return new SuccessResponse("ok");
    }

}
