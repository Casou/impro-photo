package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.CategorieMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CategorieServiceTest {

    @InjectMocks
    private CategorieService categorieService;

    @Mock
    private CategorieDao categorieDao;

    @Mock
    private CategorieMapper categorieMapper;


    @Before
    public void init() throws ImproMappingException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReinitCategoriesAndSaveThem() throws ImproServiceException {
        ArgumentCaptor<List> categorieDtoCaptor = ArgumentCaptor.forClass(List.class);
        List<CategorieDto> categories = Arrays.asList(
                CategorieDto.builder().id(BigInteger.valueOf(1)).nom("Categorie 1").type(CategorieTypeEnum.PHOTO).pathFolder("path").termine(true).ordre(1).pathInError(false).build(),
                CategorieDto.builder().id(BigInteger.valueOf(2)).nom("Categorie 2").type(CategorieTypeEnum.PHOTO).pathFolder("path").termine(false).ordre(2).pathInError(false).build(),
                CategorieDto.builder().id(BigInteger.valueOf(3)).nom("Categorie 3").type(CategorieTypeEnum.PHOTO).pathFolder("path").termine(true).ordre(3).pathInError(false).build()
        );

        this.categorieService.prepareCategories(categories);

        verify(this.categorieDao).deleteByIdNotIn(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)));
        verify(this.categorieDao).save(anyListOf(Categorie.class));
        verify(this.categorieMapper).toEntity(eq(Categorie.class), categorieDtoCaptor.capture());

        List<CategorieDto> allCategories = categorieDtoCaptor.getValue();
        assertEquals(3, allCategories.size());

        assertFalse(allCategories.get(0).getTermine());
        assertFalse(allCategories.get(1).getTermine());
        assertFalse(allCategories.get(2).getTermine());
    }


}