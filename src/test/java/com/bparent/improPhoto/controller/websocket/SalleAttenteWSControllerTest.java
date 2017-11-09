package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.json.ActionDto;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.NewScreenDto;
import com.bparent.improPhoto.service.EtatImproService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class SalleAttenteWSControllerTest {


    @InjectMocks
    private SalleAttenteWSController salleAttenteWSController;

    @Mock
    private EtatImproService etatImproService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldUpdateStatusAndLanchImpro() {
        salleAttenteWSController.launchImpro(new NewScreenDto("NEXT_SCREEN"));

        verify(etatImproService).updateStatus("ecran", "NEXT_SCREEN");
    }

    @Test
    public void shouldTransmitBoolean() {
        MessageResponse response = salleAttenteWSController.toggleAnimation(new ActionDto("true"));
        assertEquals("true", response.getMessage());
    }

}