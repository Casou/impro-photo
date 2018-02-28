package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategorieAjaxController {

    @RequestMapping(value = "/getPictures", method = RequestMethod.GET)
    public List<String> getPictures(final String pathFolder) {
        return Arrays.stream(
                new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + pathFolder)
                        .listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(FileUtils::getFrontFilePath) // ==> /photos
                .limit(IConstants.LIMIT_PICTURES)
                .collect(Collectors.toList());
    }


}
