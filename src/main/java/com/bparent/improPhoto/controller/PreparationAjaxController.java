package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.form.PreparationForm;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.PreparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PreparationAjaxController {

    @Autowired
    private PreparationService preparationService;

    @PostMapping(value = "/preparation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> savePreparation(@RequestBody PreparationForm form) throws ImproControllerException {

        try {
            preparationService.prepareImpro(form.getCategories(), form.getRemerciements(), form.getDatesImpro());
        } catch (ImproServiceException e) {
            throw new ImproControllerException("Error while saving preparation impro", e);
        }

        return new SuccessResponse("ok");
    }

}
