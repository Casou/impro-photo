package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategorieMapper extends BasicMapper<CategorieDto, Categorie> {

    public CategorieDto map(Categorie entity) throws ImproMappingException {
        return this.map(CategorieDto.class, entity);
    }

    public List<CategorieDto> map(List<Categorie> entities) throws ImproMappingException {
        List<CategorieDto> list = new ArrayList<>();
        for (Categorie entity : entities) {
            CategorieDto dto = this.map(CategorieDto.class, entity);
            dto.setPathInError(!new File(dto.getPathFolder()).exists());
            list.add(dto);
        }

        return list;
    }

}
