package com.bparent.improPhoto.enums;

import com.bparent.improPhoto.dto.ParametreDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DefaultParameters {
    MODAL_LOADING(ParametreDto.builder().context("GENERAL").key("MODAL_LOADING").valueType(ParametreType.BOOLEAN).value("false").build()),
    UPDATE_URL(ParametreDto.builder().context("GENERAL").key("UPDATE_URL").valueType(ParametreType.STRING).value(null).build());

    private ParametreDto parametre;
}
