package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.RemerciementsDto;
import com.bparent.improPhoto.dto.form.PreparationForm;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.RemerciementService;
import com.bparent.improPhoto.service.StatutPreparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RemerciementsAjaxController {

    @Autowired
    private StatutPreparationService statutPreparationService;

    @Autowired
    private RemerciementService remerciementService;

    @GetMapping("/remerciementsInfo")
    public RemerciementsDto getRemerciementsInfo() throws ImproServiceException {
        return RemerciementsDto.builder()
                .photosJoueurs(statutPreparationService.getPathPhotosJoueurs())
                .texte(statutPreparationService.getRemerciements())
                .build();
    }

    @PostMapping(value = "/remerciements", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> postRemerciements(@RequestBody RemerciementsDto dto) throws ImproServiceException {
        remerciementService.saveRemerciement(dto.getTexte());
        return new SuccessResponse("ok");
    }

}
