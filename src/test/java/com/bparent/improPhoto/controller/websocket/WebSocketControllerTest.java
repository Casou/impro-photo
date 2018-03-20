package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.service.EtatImproService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class WebSocketControllerTest {

    @InjectMocks
    private ControlPanelWSController webSocketController;

    @Mock
    private EtatImproService etatImproService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

}