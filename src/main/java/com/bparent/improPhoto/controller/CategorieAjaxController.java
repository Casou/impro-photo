package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.dto.RemerciementDto;
import com.bparent.improPhoto.dto.SongDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.mapper.BasicMapper;
import com.bparent.improPhoto.mapper.DateImproMapper;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CategorieService categorieService;


    @RequestMapping(value = "/getPictures", method = RequestMethod.GET)
    public List<String> getPictures(CategorieDto categorieDto) throws ImproMappingException {
        return Arrays.stream(
                new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorieDto.getPathFolder())
                        .listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                )
                .map(file -> file.getPath()) // ==> /photos
                .map(filePath -> FileUtils.formatPathWithCharacter("/handler/" + filePath, "/"))
                .limit(IConstants.LIMIT_PICTURES)
                .collect(Collectors.toList());
    }


}
