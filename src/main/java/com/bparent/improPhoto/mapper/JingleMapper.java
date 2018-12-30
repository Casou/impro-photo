package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Jingle;
import com.bparent.improPhoto.dto.JingleDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JingleMapper extends BasicMapper<JingleDto, Jingle> {

    public List<JingleDto> toDto(List<Jingle> jingles) throws ImproMappingException {
        return super.toDto(JingleDto.class, jingles);
    }

}
