package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.domain.ParametrePK;
import com.bparent.improPhoto.dto.ParametreDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParametreMapper extends BasicMapper<ParametreDto, Parametre> {

    public ParametreDto toDto(Parametre entity) {
        ParametreDto dto = (ParametreDto) this.map(ParametreDto.class, entity);
        dto.setContext(entity.getId().getContext());
        dto.setKey(entity.getId().getKey());
        return dto;
    }

    public List<ParametreDto> toDto(List<Parametre> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Parametre toEntity(ParametreDto dto) {
        Parametre entity = (Parametre) this.map(Parametre.class, dto);
        entity.setId(new ParametrePK(dto.getContext(), dto.getKey()));
        return entity;
    }

    public List<Parametre> toEntity(List<ParametreDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

}
