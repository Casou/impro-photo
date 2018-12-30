package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.JingleCategory;
import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JingleCategoryMapper extends BasicMapper<JingleCategoryDto, JingleCategory> {

    @Autowired
    private JingleMapper jingleMapper;

    public JingleCategoryDto toDto(JingleCategory jingleCategory) throws ImproMappingException {
        JingleCategoryDto jingleCategoryDto = super.toDto(JingleCategoryDto.class, jingleCategory);
        jingleCategoryDto.setJingles(jingleMapper.toDto(jingleCategory.getJingles()));
        return jingleCategoryDto;
    }

    public List<JingleCategoryDto> toDto(List<JingleCategory> jingleCategories) throws ImproMappingException {
        return jingleCategories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
