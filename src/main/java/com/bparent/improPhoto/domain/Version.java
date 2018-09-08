package com.bparent.improPhoto.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name="imp_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Version {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NonNull
    private String buildNumber;
    private Date buildDate;
    private String buildDescription;
    private String urlDownload;

}
