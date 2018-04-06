package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class CategorieAjaxController {

    private static final Predicate<String> isAcceptedSongFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension);

    @RequestMapping(value = "/categories/getPictures", method = RequestMethod.GET)
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


}
