package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.EtatImproDao;
import com.bparent.improPhoto.dao.MusiqueDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.domain.EtatImpro;
import com.bparent.improPhoto.domain.Musique;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.MusiqueDto;
import com.bparent.improPhoto.util.IConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtatImproService {

    @Autowired
    private EtatImproDao etatImproDao;

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private MusiqueDao musiqueDao;


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
            case IConstants.IEtatImproField.ID_CATEGORIE:
                etatImproDto.setIdCategorie(StringUtils.isEmpty(etat.getValeur()) ? null : Integer.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.TYPE_ECRAN :
                etatImproDto.setTypeEcran(etat.getValeur());
                break;
            case IConstants.IEtatImproField.BLOCK_MASQUES :
                etatImproDto.setBlockMasques(StringUtils.isEmpty(etat.getValeur()) ? new ArrayList<>() :
                        Arrays.stream(etat.getValeur().split(","))
                            .map(blockId -> BigInteger.valueOf(Integer.valueOf(blockId)))
                            .collect(Collectors.toList()));
                break;
            case IConstants.IEtatImproField.INTEGRALITE :
                etatImproDto.setIntegralite(StringUtils.isEmpty(etat.getValeur()) ? false : Boolean.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.PHOTO_COURANTE :
                etatImproDto.setPhotoCourante(StringUtils.isEmpty(etat.getValeur()) ? null : Integer.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.PHOTOS_CHOISIES :
                etatImproDto.setPhotosChoisies(StringUtils.isEmpty(etat.getValeur()) ? new ArrayList<>() :
                        Arrays.stream(etat.getValeur().split(","))
                            .map(blockId -> BigInteger.valueOf(Integer.valueOf(blockId)))
                            .collect(Collectors.toList()));
                break;
            case IConstants.IEtatImproField.STATUT_DIAPO :
                etatImproDto.setStatutDiapo(etat.getValeur());
                break;
            case IConstants.IEtatImproField.CURRENT_SONG :
                if (etat.getValeur() == null) {
                    break;
                }
                final BigInteger songId = new BigInteger(etat.getValeur());
                etatImproDto.setCurrentSongId(songId);
                Musique musique = musiqueDao.findOne(songId);
                if (musique == null) {
                    break;
                }
                etatImproDto.setCurrentSong(new MusiqueDto(musique));
                break;
            case IConstants.IEtatImproField.PLAYLIST_VOLUME :
                etatImproDto.setPlaylistVolume(Integer.valueOf(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.PLAYLIST_STATUS :
                etatImproDto.setIsPlaylistPlaying(IConstants.IEtatImproField.IStatutPlaylist.PLAYING.equals(etat.getValeur()));
                break;
            case IConstants.IEtatImproField.CATEGORIES_SHOWN :
                etatImproDto.setCategoriesShown(Boolean.valueOf(etat.getValeur()));
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

    public void updateStatus(String fieldId, List<BigInteger> values) {
        this.updateStatus(fieldId,
                values.stream()
                        .map(BigInteger::toString)
                        .collect(Collectors.joining(",")));
    }

    public void resetImpro() {
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.ECRAN, IConstants.IImpro.FIRST_SCREEN));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.ID_CATEGORIE, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.TYPE_ECRAN, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.BLOCK_MASQUES, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.INTEGRALITE, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.PHOTO_COURANTE, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.PHOTOS_CHOISIES, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.STATUT_DIAPO, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.CATEGORIES_SHOWN, String.valueOf(false)));

        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.CURRENT_SONG, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.PLAYLIST_STATUS, null));
        etatImproDao.save(new EtatImpro(IConstants.IEtatImproField.PLAYLIST_VOLUME, String.valueOf(10)));

        List<Categorie> allCategories = categorieDao.findAll();
        allCategories.forEach(categorie -> categorie.setTermine(false));
        categorieDao.save(allCategories);
    }
}
