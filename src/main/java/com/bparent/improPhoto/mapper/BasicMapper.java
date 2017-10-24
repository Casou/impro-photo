package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.exception.ImproException;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@NoArgsConstructor
public class BasicMapper<DTO, ENTITY> {

    public DTO map(Class<DTO> dtoClass, ENTITY entity) throws ImproException {
        DTO dto;
        try {
            dto = dtoClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ImproException("Erreur lors de l'instanciation du DTO", e);
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
                throw new ImproException("Erreur lors de la récupération du champ " + field.getName() + " dans l'objet " + entity, e);
            }

            try {
                dtoField.setAccessible(true);
                dtoField.set(dto, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new ImproException("Erreur lors du remplissage du champ " + field.getName() + " dans l'objet " + dto, e);
            }
        }

        return dto;
    }

}
