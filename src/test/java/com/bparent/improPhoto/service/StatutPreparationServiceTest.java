package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproServiceException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class StatutPreparationServiceTest {

    @InjectMocks
    private StatutPreparationService statutPreparationService;

    @Mock
    private CategorieDao categorieDao;

    @Mock
    private CategorieService categorieService;

    @Mock
    private RemerciementService remerciementService;

    @Mock
    private EtatImproService etatImproService;


    @Before
    public void init() throws ImproServiceException {
        MockitoAnnotations.initMocks(this);
        when(categorieDao.findAll()).thenReturn(new ArrayList<>());
        when(remerciementService.getRemerciements()).thenReturn("");
        when(etatImproService.getStatut()).thenReturn(new EtatImproDto());
    }


    @Test
    public void shouldCheckCategoriesFalse() throws ImproServiceException {
        when(categorieDao.findAll()).thenReturn(Arrays.asList(
                new Categorie(BigInteger.valueOf(1), "nom1", CategorieTypeEnum.PHOTO, "pathFolder1", false, 1),
                new Categorie(BigInteger.valueOf(2), "nom2", CategorieTypeEnum.PHOTO, "pathFolder2", false, 2)
                ));
        StatutPreparationDto statutPreparation = statutPreparationService.getStatutPreparation();
        assertFalse(statutPreparation.getCategories());
    }

    // TODO Trouver un moyen de mocker les recherches de fichier
    @Ignore
    @Test
    public void shouldCheckVideosFalse() throws ImproServiceException {
        StatutPreparationDto statutPreparation = statutPreparationService.getStatutPreparation();
        assertFalse(statutPreparation.getVideoPresentationJoueurs());
        assertFalse(statutPreparation.getVideoPresentationPresentateur());
    }

    // TODO Trouver un moyen de mocker les recherches de fichier
    @Ignore
    @Test
    public void shouldGet0Photos() throws ImproServiceException {
        StatutPreparationDto statutPreparation = statutPreparationService.getStatutPreparation();
        assertEquals(Integer.valueOf(0), statutPreparation.getPhotosJoueurs());
        assertEquals(Integer.valueOf(0), statutPreparation.getPhotosPresentationDates());
    }

}