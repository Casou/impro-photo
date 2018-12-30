package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Jingle;
import com.bparent.improPhoto.dto.JingleDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JingleMapper extends BasicMapper<JingleDto, Jingle> {

    public JingleDto toDto(Jingle jingle) throws ImproMappingException {
        JingleDto jingleDto = super.toDto(JingleDto.class, jingle);
        jingleDto.setFolder(jingle.getCategory().getFolderName());
        return jingleDto;
    }

    public List<JingleDto> toDto(List<Jingle> jingles) throws ImproMappingException {
        return jingles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
