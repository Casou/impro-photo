package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategorieMapper extends BasicMapper<CategorieDto, Categorie> {

    public CategorieDto toDto(Categorie entity) throws ImproMappingException {
        CategorieDto categorieDto = super.toDto(CategorieDto.class, entity);
        categorieDto.setPathInError(!new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + categorieDto.getPathFolder()).exists());
        return categorieDto;
    }

    public List<CategorieDto> toDto(List<Categorie> entities) throws ImproMappingException {
        List<CategorieDto> list = new ArrayList<>();
        for (Categorie entity : entities) {
            list.add(this.toDto(entity));
        }

        return list;
    }

    public Categorie toEntity(CategorieDto dto) throws ImproMappingException {
        Categorie entity = super.toEntity(Categorie.class, dto);
        return entity;
    }

    public List<Categorie> toEntity(List<CategorieDto> entities) throws ImproMappingException {
        List<Categorie> list = new ArrayList<>();
        for (CategorieDto dto : entities) {
            list.add(this.toEntity(dto));
        }

        return list;
    }

}
