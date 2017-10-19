package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.EtatImproDao;
import com.bparent.improPhoto.domain.EtatImpro;
import com.bparent.improPhoto.dto.EtatImproDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class EtatImproService {

    @Autowired
    private EtatImproDao statutDao;

    public EtatImproDto getStatut() {
        EtatImproDto etatImpro = new EtatImproDto();
        Iterable<EtatImpro> allEtatImproProperties = this.statutDao.findAll();
        allEtatImproProperties.forEach(st -> fillDto(etatImpro, st));

        return etatImpro;
    }

    private void fillDto(EtatImproDto etatImproDto, EtatImpro etat) {
        switch (etat.getChamp()) {
            case "ecran" :
                etatImproDto.setEcran(etat.getValeur());
                break;
            case "id_ecran" :
                etatImproDto.setIdEcran(Integer.valueOf(etat.getValeur()));
                break;
            case "type_ecran" :
                etatImproDto.setTypeEcran(etat.getValeur());
                break;
            case "block_masques" :
                etatImproDto.setBlockMasques(Arrays.stream(etat.getValeur().split(","))
                        .map(blockId -> Integer.valueOf(blockId))
                        .collect(Collectors.toList()));
                break;
            case "integralite" :
                etatImproDto.setIntegralite(Boolean.valueOf(etat.getValeur()));
                break;
            case "photo_courante" :
                etatImproDto.setPhotoCourante(Integer.valueOf(etat.getValeur()));
                break;
            case "photos_choisies" :
                etatImproDto.setPhotosChoisies(Arrays.stream(etat.getValeur().split(","))
                        .map(blockId -> Integer.valueOf(blockId))
                        .collect(Collectors.toList()));
                break;
            case "statut_diapo" :
                etatImproDto.setStatutDiapo(etat.getValeur());
                break;

            default:
                break;
        }
    }

}
