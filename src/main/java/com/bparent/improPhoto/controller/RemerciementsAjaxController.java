package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.RemerciementsDto;
import com.bparent.improPhoto.service.StatutPreparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemerciementsAjaxController {

    @Autowired
    private StatutPreparationService statutPreparationService;

    @RequestMapping(value = "/remerciementsInfo", method = RequestMethod.GET)
    public RemerciementsDto getRemerciementsInfo() {
        return RemerciementsDto.builder()
                .photosJoueurs(statutPreparationService.getPathPhotosJoueurs())
                .texte(statutPreparationService.getRemerciements())
                .build();
    }

}
