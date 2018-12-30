package com.bparent.improPhoto.domain;


import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="imp_jingle")
@Data
public class Jingle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NonNull
    private String name;

    @NonNull
    private String fileName;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private JingleCategory category;

}
