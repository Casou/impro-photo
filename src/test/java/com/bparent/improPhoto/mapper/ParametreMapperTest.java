package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.domain.ParametrePK;
import com.bparent.improPhoto.enums.ParametreType;
import com.bparent.improPhoto.exception.ImproMappingException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParametreMapperTest {

    @Test
    public void toDto_shouldMapId() throws ImproMappingException {
        ParametreMapper parametreMapper = new ParametreMapper();
        com.bparent.improPhoto.dto.ParametreDto dto = parametreMapper.toDto(Parametre.builder().id(new ParametrePK("GENERAL", "MODAL_LOADING")).valueType(ParametreType.BOOLEAN).value("false").build());

        assertEquals("false", dto.getValue());
        assertEquals(ParametreType.BOOLEAN, dto.getValueType());
        assertNull(dto.getDescription());
        assertEquals("GENERAL", dto.getContext());
        assertEquals("MODAL_LOADING", dto.getKey());
    }

    @Test
    public void toEntity_shouldMapId() throws ImproMappingException {
        ParametreMapper parametreMapper = new ParametreMapper();
        Parametre entity = parametreMapper.toEntity(com.bparent.improPhoto.dto.ParametreDto.builder().context("GENERAL").key("MODAL_LOADING").valueType(ParametreType.BOOLEAN).value("false").build());

        assertEquals("false", entity.getValue());
        assertEquals(ParametreType.BOOLEAN, entity.getValueType());
        assertNull(entity.getDescription());
        assertEquals("GENERAL", entity.getId().getContext());
        assertEquals("MODAL_LOADING", entity.getId().getKey());
    }

}