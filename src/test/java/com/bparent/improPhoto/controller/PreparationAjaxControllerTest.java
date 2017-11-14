package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.dto.RemerciementDto;
import com.bparent.improPhoto.dto.form.PreparationForm;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.service.PreparationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;

public class PreparationAjaxControllerTest {

    @InjectMocks
    private PreparationAjaxController preparationAjaxController;

    @Mock
    private PreparationService preparationService;

    @Mock
    private EtatImproService etatImproService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCallServices() throws ImproControllerException, ImproServiceException {
        this.preparationAjaxController.savePreparation(buildForm());

        Mockito.verify(preparationService).prepareImpro(anyListOf(CategorieDto.class), any(RemerciementDto.class), anyListOf(DateImproDto.class));
        Mockito.verify(etatImproService).resetImpro();
    }

    private PreparationForm buildForm() {
        return new PreparationForm();
    }

}