package com.bparent.improPhoto.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name="imp_dates")
@Data
@NoArgsConstructor
public class DateImpro {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private BigInteger id;

    @NonNull
    @Column(name = "date")
    private Date date;

    @NonNull
    @Column(name = "nom")
    private String nom;

}
