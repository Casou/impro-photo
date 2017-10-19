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
    @NonNull
    @Column(name = "texte")
    private String texte;

}
