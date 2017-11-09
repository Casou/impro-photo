package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.service.StatutPreparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainAjaxController {

    @Autowired
    private EtatImproService etatImproService;

    @Autowired
    private StatutPreparationService statutPreparationService;


    @RequestMapping(value = "/statutPreparation", method = RequestMethod.GET)
    public StatutPreparationDto getStatutPreparation() {
        return statutPreparationService.getStatutPreparation();
    }

    @RequestMapping(value = "/etatImpro", method = RequestMethod.GET)
    public EtatImproDto getEtatImpro() {
        return etatImproService.getStatut();
    }

}
