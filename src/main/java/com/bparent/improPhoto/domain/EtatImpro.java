package com.bparent.improPhoto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name="imp_statut")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtatImpro {

    @Id
    @NonNull
    @Column(name = "champ")
    private String champ;

    @Column(name = "valeur")
    private String valeur;

}
