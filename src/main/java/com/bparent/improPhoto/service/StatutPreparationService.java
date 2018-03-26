package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatutPreparationService {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private RemerciementDao remerciementDao;

    public StatutPreparationDto getStatutPreparation() throws ImproServiceException {
        StatutPreparationDto dto = new StatutPreparationDto();
        dto.setCategories(checkCategories());
        dto.setVideoPresentationJoueurs(checkVideo(IConstants.IPath.IVideo.VIDEO_INTRO_JOUEURS));
        dto.setVideoPresentationPresentateur(checkVideo(IConstants.IPath.IVideo.VIDEO_INTRO_PRESENTATEUR));
        dto.setPhotosJoueurs(getNbPhotosJoueurs());
        dto.setRemerciements(getRemerciements());
        dto.setPhotosPresentationDates(getNbPhotosPresentationDates());
        return dto;
    }

    private Boolean checkCategories() throws ImproServiceException {
        int nbCategories = 0;
        List<Categorie> allCategories = categorieDao.findAll();
        for (Categorie cat : allCategories) {
            File folder = new File(IConstants.IPath.IPhoto.PHOTOS_IMPRO + cat.getPathFolder());
            if (!folder.exists() || !folder.isDirectory()) {
                return false;
            }
            nbCategories++;
        }

        if (nbCategories == 0) {
            return false;
        }

        return categorieService.getMissingCategoriesFromDatabase(allCategories).isEmpty();
    }

    private Boolean checkVideo(String pathVideosIntro) {
        File folder = new File(pathVideosIntro);
        return folder.exists() && folder.isFile();
    }

    public String getRemerciements() {
        Iterator<Remerciement> iterator = remerciementDao.findAll().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        return iterator.next().getTexte();
    }

    private List<File> getListPhotosFolder(String folder) {
        return Arrays.asList(new File(folder)
                .listFiles((dir, name) -> IConstants.PICTURE_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(name.toLowerCase()))));
    }

    private Integer getNbPhotosFolder(String folder) {
        return this.getListPhotosFolder(folder).size();
    }

    public List<String> getPathPhotosJoueurs() {
        return this.getListPhotosFolder(IConstants.IPath.IPhoto.PHOTOS_JOUEURS).stream()
                .map(FileUtils::getFrontFilePath)
                .collect(Collectors.toList());
    }

    private Integer getNbPhotosJoueurs() {
        return getNbPhotosFolder(IConstants.IPath.IPhoto.PHOTOS_JOUEURS);
    }

    private Integer getNbPhotosPresentationDates() {
        return getNbPhotosFolder(IConstants.IPath.IPhoto.PHOTOS_PRESENTATION_DATES);
    }

}
