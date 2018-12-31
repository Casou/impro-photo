package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.JingleCategoryDao;
import com.bparent.improPhoto.dao.JingleDao;
import com.bparent.improPhoto.domain.Jingle;
import com.bparent.improPhoto.domain.JingleCategory;
import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.mapper.JingleCategoryMapper;
import com.bparent.improPhoto.mapper.JingleMapper;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JingleService {

    @Autowired
    private JingleCategoryDao jingleCategoryDao;

    @Autowired
    private JingleDao jingleDao;

    @Autowired
    private JingleCategoryMapper jingleCategoryMapper;

    @Autowired
    private JingleMapper jingleMapper;

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

//    private List<JingleDto> listJingles(File folder) {
//        return Arrays.stream(
//                Objects.requireNonNull(folder.listFiles((dir, name) ->
//                        IConstants.AUDIO_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase()))))
//                )
//                .map(File::getName)
//                .map(nom -> new JingleDto(nom, folder.getName()))
//                .collect(Collectors.toList());
//    }

    public Jingle saveUploadedJingle(UploadedFileDto uploadedFile, JingleCategory category) {
        return jingleDao.save(uploadedFile.toJingle(category));
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

    public void deleteJingle(BigInteger id) {
        Jingle jingle = jingleDao.findOne(id);
        FileUtils.deleteFile(new File(IConstants.IPath.IAudio.AUDIOS_JINGLES + jingle.getCategory().getFolderName()
                + "/" + jingle.getFileName()));

        jingle.getCategory().getJingles().remove(jingle);

        jingleDao.delete(jingle);
    }

    public void deleteJingleCategory(BigInteger categoryId) {
        JingleCategory category = jingleCategoryDao.findOne(categoryId);
        FileUtils.deleteFolder(new File(IConstants.IPath.IAudio.AUDIOS_JINGLES + category.getFolderName()));
        jingleCategoryDao.delete(category);
    }
}
