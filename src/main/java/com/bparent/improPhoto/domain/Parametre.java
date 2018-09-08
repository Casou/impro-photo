package com.bparent.improPhoto.domain;

import com.bparent.improPhoto.enums.ParametreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="imp_parametre")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parametre {

    @EmbeddedId
    private ParametrePK id;

    @Enumerated(EnumType.STRING)
    private ParametreType valueType;

    private String value;
    private String description;

}
