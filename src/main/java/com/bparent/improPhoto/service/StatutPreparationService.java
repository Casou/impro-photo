package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatutPreparationService {

    @Autowired
    private CategorieDao categorieDao;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private RemerciementService remerciementService;

    @Autowired
    private EtatImproService etatImproService;


    public StatutPreparationDto getStatutPreparation() throws ImproServiceException {
        StatutPreparationDto dto = new StatutPreparationDto();
        dto.setImproLaunched(checkImproLaunched());
        dto.setCategories(checkCategories());
        dto.setVideoPresentationJoueurs(checkVideo(IConstants.IPath.IVideo.VIDEO_INTRO_JOUEURS));
        dto.setVideoPresentationPresentateur(checkVideo(IConstants.IPath.IVideo.VIDEO_INTRO_PRESENTATEUR));
        dto.setPhotosJoueurs(getNbPhotosJoueurs());
        dto.setRemerciements(getRemerciements());
        dto.setPhotosPresentationDates(getNbPhotosPresentationDates());
        return dto;
    }

    private Boolean checkImproLaunched() {
        EtatImproDto statut = etatImproService.getStatut();
        List<Categorie> allCategories = categorieDao.findAll();

        return !(
                IConstants.IImpro.FIRST_SCREEN.equals(statut.getEcran()) &&
                statut.getIdCategorie() == null &&
                statut.getTypeEcran() == null &&
                !statut.getIntegralite() &&
                !statut.getCategoriesShown() &&
                statut.getBlockMasques().size() == 0 &&
                statut.getPhotosChoisies().size() == 0 &&
                statut.getPhotoCourante() == null &&
                statut.getStatutDiapo() == null &&
                !allCategories.stream().anyMatch(Categorie::getTermine)
        );
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

        return nbCategories != 0 && categorieService.getMissingCategoriesFromDatabase(allCategories).isEmpty();

    }

    public String getRemerciements() throws ImproServiceException {
        return remerciementService.getRemerciements();
    }

    private Boolean checkVideo(String pathVideosIntro) {
        File folder = new File(pathVideosIntro);
        return folder.exists() && folder.isFile();
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
