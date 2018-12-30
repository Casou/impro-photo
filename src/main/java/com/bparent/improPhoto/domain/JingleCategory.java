package com.bparent.improPhoto.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name="imp_jingle_category")
@Data
public class JingleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NonNull
    private String name;

    @NonNull
    private String folderName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Jingle> jingles;

}