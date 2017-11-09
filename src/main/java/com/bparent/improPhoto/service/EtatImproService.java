package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.EtatImproDao;
import com.bparent.improPhoto.domain.EtatImpro;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class EtatImproService {

    @Autowired
    private EtatImproDao etatImproDao;

    public EtatImproDto getStatut() {
        EtatImproDto etatImpro = new EtatImproDto();
        Iterable<EtatImpro> allEtatImproProperties = this.etatImproDao.findAll();
        allEtatImproProperties.forEach(st -> fillDto(etatImpro, st));

        return etatImpro;
    }

    private void fillDto(EtatImproDto etatImproDto, EtatImpro etat) {
        switch (etat.getChamp()) {
            case IConstants.IEtatImproField.ECRAN :
                etatImproDto.setEcran(etat.getValeur());
                break;
            case IConstants.IEtatImproField.ID_ECRAN :
                etatImproDto.setIdEcran(etat.getValeur() == null ? null : Integer.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.TYPE_ECRAN :
                etatImproDto.setTypeEcran(etat.getValeur());
                break;
            case IConstants.IEtatImproField.BLOCK_MASQUES :
                etatImproDto.setBlockMasques(etat.getValeur() == null ? null :
                        Arrays.stream(etat.getValeur().split(","))
                            .map(blockId -> Integer.valueOf(blockId))
                            .collect(Collectors.toList()));
                break;
            case IConstants.IEtatImproField.INTEGRALITE :
                etatImproDto.setIntegralite(etat.getValeur() == null ? null : Boolean.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.PHOTO_COURANTE :
                etatImproDto.setPhotoCourante(etat.getValeur() == null ? null : Integer.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.PHOTOS_CHOISIES :
                etatImproDto.setPhotosChoisies(etat.getValeur() == null ? null :
                        Arrays.stream(etat.getValeur().split(","))
                            .map(blockId -> Integer.valueOf(blockId))
                            .collect(Collectors.toList()));
                break;
            case IConstants.IEtatImproField.STATUT_DIAPO :
                etatImproDto.setStatutDiapo(etat.getValeur());
                break;
            default:
                break;
        }
    }

    public void updateStatus(String fieldId, String value) {
        EtatImpro field = etatImproDao.findOne(fieldId);
        if (field == null) {
            field = new EtatImpro();
            field.setChamp(fieldId);
        }
        field.setValeur(value);

        etatImproDao.save(field);
    }

    public void resetImpro() {
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.ECRAN, IConstants.IImpro.FIRST_SCREEN));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.ID_ECRAN, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.TYPE_ECRAN, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.BLOCK_MASQUES, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.INTEGRALITE, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.PHOTO_COURANTE, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.PHOTOS_CHOISIES, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.STATUT_DIAPO, null));
    }
}
