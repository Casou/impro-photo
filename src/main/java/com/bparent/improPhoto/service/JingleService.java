package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.JingleCategoryDao;
import com.bparent.improPhoto.domain.Jingle;
import com.bparent.improPhoto.domain.JingleCategory;
import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.dto.JingleDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.mapper.JingleCategoryMapper;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JingleService {

    @Autowired
    private JingleCategoryDao jingleCategoryDao;

    @Autowired
    private JingleCategoryMapper jingleCategoryMapper;

    public List<JingleCategoryDto> getAllJingles() {
        return jingleCategoryMapper.toDto(jingleCategoryDao.findAll());

//        List<File> folders = Arrays.stream(new File(IConstants.IPath.IAudio.AUDIOS_JINGLES)
//                .listFiles((file) -> file.isDirectory()))
//                .collect(Collectors.toList());
//
//        return folders.stream().map(folder ->
//                JingleCategoryDto.builder()
//                        .nom(folder.getName())
//                        .jingles(listJingles(folder))
//                        .build()).collect(Collectors.toList());

    }

    private List<JingleDto> listJingles(File folder) {
        return Arrays.stream(
                Objects.requireNonNull(folder.listFiles((dir, name) ->
                        IConstants.AUDIO_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase()))))
                )
                .map(File::getName)
                .map(nom -> new JingleDto(nom, folder.getName()))
                .collect(Collectors.toList());
    }

    public List<JingleCategory> saveUploadedJingles(List<UploadedFileDto> uploadedFiles) {
        List<JingleCategory> jingleCategories = uploadedFiles.stream()
                .map(category -> {
                    JingleCategory jingleCategory = JingleCategory.builder()
                            .name(category.getName())
                            .folderName(category.getFileName())
                            .build();

                    jingleCategory.setJingles(category.getChildren().stream()
                            .map(file -> Jingle.builder()
                                    .name(file.getName())
                                    .fileName(file.getFileName())
                                    .category(jingleCategory)
                                    .build())
                            .collect(Collectors.toList()));

                    return jingleCategory;
                })
                .collect(Collectors.toList());

        jingleCategoryDao.save(jingleCategories);
        return jingleCategories;
    }

    public void deleteJingleCategory(JingleCategoryDto jingleCategoryDto) {
        FileUtils.deleteFolder(new File(IConstants.IPath.IAudio.AUDIOS_JINGLES + jingleCategoryDto.getName()));
    }
}
