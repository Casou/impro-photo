package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
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
    private RemerciementDao remerciementDao;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(categorieDao.findAll()).thenReturn(new ArrayList<>());
        when(remerciementDao.findAll()).thenReturn(new ArrayList<>());
    }


    @Test
    public void shouldCheckCategoriesFalse() {
        when(categorieDao.findAll()).thenReturn(Arrays.asList(
                new Categorie(BigInteger.valueOf(1), "nom1", CategorieTypeEnum.PHOTO, "pathFolder1", false, 1),
                new Categorie(BigInteger.valueOf(2), "nom2", CategorieTypeEnum.PHOTO, "pathFolder2", false, 2)
                ));
        StatutPreparationDto statutPreparation = statutPreparationService.getStatutPreparation();
        assertFalse(statutPreparation.getCategories());
    }

    @Test
    public void shouldCheckVideosFalse() {
        StatutPreparationDto statutPreparation = statutPreparationService.getStatutPreparation();
        assertFalse(statutPreparation.getVideoPresentationJoueurs());
        assertFalse(statutPreparation.getVideoPresentationPresentateur());
    }

    // TODO Trouver un moyen de mocker les recherches de fichier
    @Ignore
    @Test
    public void shouldGet0Photos() {
        StatutPreparationDto statutPreparation = statutPreparationService.getStatutPreparation();
        assertEquals(Integer.valueOf(0), statutPreparation.getPhotosJoueurs());
        assertEquals(Integer.valueOf(0), statutPreparation.getPhotosPresentationDates());
    }

}