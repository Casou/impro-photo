package com.bparent.improPhoto.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="imp_remerciements")
@Data
@NoArgsConstructor
public class Remerciement {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "texte")
    private String texte;

}
