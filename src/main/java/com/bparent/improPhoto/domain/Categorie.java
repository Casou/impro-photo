package com.bparent.improPhoto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="imp_categorie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private BigInteger id;

    @NonNull
    @Column(name = "nom")
    private String nom;

    @NonNull
    @Column(name = "type")
    private String type;

    @NonNull
    @Column(name = "path_folder")
    private String pathFolder;

    @NonNull
    @Column(name = "termine")
    private Boolean termine = Boolean.FALSE;

    @NonNull
    @Column(name = "ordre")
    private Integer ordre = 99;

}
