package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.enums.ParametreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametreDto {

    private String context;
    private String key;
    private ParametreType valueType;
    private String value;
    private String description;

    @Override
    public boolean equals(Object o) {
        ParametreDto dto = (ParametreDto) o;
        return this.context.equals(dto.getContext()) && this.key.equals(dto.getKey());
    }

}
