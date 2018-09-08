package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.dto.JingleDto;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JingleService {

    public List<JingleCategoryDto> getAllJingles() {
        List<File> folders = Arrays.stream(new File(IConstants.IPath.IAudio.AUDIOS_JINGLES)
                .listFiles((file) -> file.isDirectory()))
                .collect(Collectors.toList());

        return folders.stream().map(folder ->
                JingleCategoryDto.builder()
                        .nom(folder.getName())
                        .jingles(listJingles(folder))
                        .build()).collect(Collectors.toList());

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

    public void deleteJingleCategory(JingleCategoryDto jingleCategoryDto) {
        FileUtils.deleteFolder(new File(IConstants.IPath.IAudio.AUDIOS_JINGLES + jingleCategoryDto.getNom()));
    }
}
