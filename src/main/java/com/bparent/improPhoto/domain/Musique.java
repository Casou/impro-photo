package com.bparent.improPhoto.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="imp_musique")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Musique {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NonNull
    private String name;

    @NonNull
    private String fileName;

}
