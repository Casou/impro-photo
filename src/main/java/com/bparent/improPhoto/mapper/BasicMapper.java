package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.exception.ImproMappingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class BasicMapper<DTO, ENTITY> {

    public DTO toDto(Class<DTO> dtoClass, ENTITY entity) throws ImproMappingException {
        return (DTO) this.map(dtoClass, entity);
    }

    public List<DTO> toDto(Class<DTO> dtoClass, List<ENTITY> entities) throws ImproMappingException {
        return entities.stream()
                .map(entity -> this.toDto(dtoClass, entity))
                .collect(Collectors.toList());
    }


    public ENTITY toEntity(Class<ENTITY> dtoClass, DTO dto) throws ImproMappingException {
        return (ENTITY) this.map(dtoClass, dto);
    }

    public List<ENTITY> toEntity(Class<ENTITY> dtoClass, List<DTO> dtos) throws ImproMappingException {
        return dtos.stream()
                .map(dto -> this.toEntity(dtoClass, dto))
                .collect(Collectors.toList());
    }


    protected Object map(Class<?> dtoClass, Object entity) throws ImproMappingException {
        Object dto;
        try {
            dto = dtoClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ImproMappingException("Erreur lors de l'instanciation du DTO", e);
        }

        for (Field field : entity.getClass().getDeclaredFields()) {
            Field dtoField;
            try {
                dtoField = dto.getClass().getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                continue;
            }


            Object value;
            try {
                field.setAccessible(true);
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new ImproMappingException("Erreur lors de la récupération du champ " + field.getName() + " dans l'objet " + entity, e);
            }

            try {
                dtoField.setAccessible(true);
                dtoField.set(dto, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new ImproMappingException("Erreur lors du remplissage du champ " + field.getName() + " dans l'objet " + dto, e);
            }
        }

        return dto;
    }

}
