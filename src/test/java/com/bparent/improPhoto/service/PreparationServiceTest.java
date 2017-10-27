package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.dao.RemerciementDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.domain.DateImpro;
import com.bparent.improPhoto.domain.Remerciement;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.dto.RemerciementDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.CategorieMapper;
import com.bparent.improPhoto.mapper.DateImproMapper;
import com.bparent.improPhoto.mapper.RemerciementMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

public class PreparationServiceTest {

    @InjectMocks
    private PreparationService preparationService;

    @Mock
    private CategorieMapper categorieMapper;

    @Mock
    private CategorieDao categorieDao;

    @Mock
    private RemerciementMapper remerciementMapper;

    @Mock
    private RemerciementDao remerciementDao;

    @Mock
    private DateImproDao dateImproDao;

    @Mock
    private DateImproMapper dateImproMapper;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


    @Before
    public void init() throws ImproMappingException {
        MockitoAnnotations.initMocks(this);

        Mockito.doCallRealMethod().when(categorieMapper).toEntity(any(CategorieDto.class));
        Mockito.doCallRealMethod().when(categorieMapper).toEntity(anyList());
        Mockito.doCallRealMethod().when(remerciementMapper).toEntity(eq(Remerciement.class), any(RemerciementDto.class));
        Mockito.doCallRealMethod().when(dateImproMapper).toEntity(eq(DateImpro.class), anyList());
    }

    @Test
    public void shouldReinitCategoriesAndSaveThem() throws ImproServiceException {
        ArgumentCaptor<List> categorieDtoCaptor = ArgumentCaptor.forClass(List.class);
        List<CategorieDto> categories = Arrays.asList(
                new CategorieDto(BigInteger.valueOf(1), "Categorie 1", CategorieTypeEnum.PHOTO, "path", true, 1, false),
                new CategorieDto(BigInteger.valueOf(2), "Categorie 2", CategorieTypeEnum.PHOTO, "path", false, 2, false),
                new CategorieDto(BigInteger.valueOf(3), "Categorie 3", CategorieTypeEnum.PHOTO, "path", true, 3, false)
        );

        this.preparationService.prepareImpro(categories, new RemerciementDto(), Arrays.asList());

        verify(this.categorieDao).deleteByIdNotIn(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)));
        verify(this.categorieDao).save(categorieDtoCaptor.capture());

        List<Categorie> allCategories = categorieDtoCaptor.getValue();
        assertEquals(3, allCategories.size());

        assertFalse(allCategories.get(0).getTermine());
        assertFalse(allCategories.get(1).getTermine());
        assertFalse(allCategories.get(2).getTermine());
    }

    @Test
    public void shouldSaveRemerciements() throws ImproServiceException {
        ArgumentCaptor<Remerciement> remerciementCaptor = ArgumentCaptor.forClass(Remerciement.class);
        this.preparationService.prepareImpro(Arrays.asList(), new RemerciementDto(1, "Test remerciements"), Arrays.asList());

        verify(this.remerciementDao).deleteByIdNotIn(Arrays.asList(1));
        verify(this.remerciementDao).save(remerciementCaptor.capture());

        Remerciement remerciement = remerciementCaptor.getValue();
        assertEquals(Integer.valueOf(1), remerciement.getId());
        assertEquals("Test remerciements", remerciement.getTexte());
    }

    @Test
    public void shouldSaveDates() throws ImproServiceException, ParseException {
        ArgumentCaptor<List> dateImproCaptor = ArgumentCaptor.forClass(List.class);
        this.preparationService.prepareImpro(Arrays.asList(), new RemerciementDto(), Arrays.asList(
                new DateImproDto(BigInteger.valueOf(1), dateFormatter.parse("06/01/2018"), "Prochaine date"),
                new DateImproDto(BigInteger.valueOf(2), dateFormatter.parse("22/05/2018"), "Date d'après")
        ));

        verify(this.dateImproDao).deleteByIdNotIn(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2)));
        verify(this.dateImproDao).save(dateImproCaptor.capture());

        List<DateImpro> allDates = dateImproCaptor.getValue();
        assertEquals(2, allDates.size());

        assertEquals(dateFormatter.parse("06/01/2018"), allDates.get(0).getDate());
        assertEquals(dateFormatter.parse("22/05/2018"), allDates.get(1).getDate());
    }

}