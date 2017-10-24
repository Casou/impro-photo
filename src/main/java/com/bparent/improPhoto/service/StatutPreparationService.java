package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class StatutPreparationService {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private RemerciementDao remerciementDao;

    private static final List<String> PICTURE_EXTENSION_ACCEPTED = Arrays.asList(".jpg", ".jpeg", ".png", ".bmp", ".gif");

    public StatutPreparationDto getStatutPreparation() {
        StatutPreparationDto dto = new StatutPreparationDto();
        dto.setCategories(checkCategories());
        dto.setVideoPresentationJoueurs(checkVideo(IConstants.PATH_VIDEOS_INTRO_JOUEURS));
        dto.setVideoPresentationPresentateur(checkVideo(IConstants.PATH_VIDEOS_INTRO_PRESENTATEUR));
        dto.setPhotosJoueurs(getNbPhotosJoueurs());
        dto.setRemerciements(getRemerciements());
        dto.setPhotosPresentationDates(getNbPhotosPresentationDates());
        return dto;
    }

    private Boolean checkCategories() {
        int nbCategories = 0;
        boolean categorieOk = true;
        for (Categorie cat : categorieDao.findAll()) {
            File folder = new File(IConstants.PATH_PHOTOS + cat.getPathFolder());
            if (!folder.exists() || !folder.isDirectory()) {
                categorieOk = false;
                break;
            }
            nbCategories++;
        };

        if (categorieOk && nbCategories == 0) {
            categorieOk = false;
        }
        return categorieOk;
    }

    private Boolean checkVideo(String pathVideosIntro) {
        File folder = new File(IConstants.PATH_PHOTOS + pathVideosIntro);
        return folder.exists() && folder.isFile();
    }

    private String getRemerciements() {
        Iterator<Remerciement> iterator = remerciementDao.findAll().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        return iterator.next().getTexte();
    }

    private Integer getNbPhotosFolder(String folder) {
        return new File(folder)
                .listFiles((dir, name) -> PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase())))
                .length;
    }

    private Integer getNbPhotosJoueurs() {
        return getNbPhotosFolder(IConstants.PATH_PHOTOS_JOUEURS);
    }

    private Integer getNbPhotosPresentationDates() {
        return getNbPhotosFolder(IConstants.PATH_PHOTOS_PRESENTATION_DATES);
    }

}
