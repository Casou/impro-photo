package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategorieMapper extends BasicMapper<CategorieDto, Categorie> {

    public CategorieDto toDto(Categorie entity) throws ImproMappingException {
        CategorieDto categorieDto = super.toDto(CategorieDto.class, entity);
        categorieDto.setPathInError(!new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorieDto.getPathFolder()).exists());
        return categorieDto;
    }

    public List<CategorieDto> toDto(List<Categorie> entities) throws ImproMappingException {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
