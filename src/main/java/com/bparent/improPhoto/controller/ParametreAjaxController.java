package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.ParametreDao;
import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParametreAjaxController {

    @Autowired
    private ParametreDao parametreDao;

    @PutMapping(value = "/parametre", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> savePreparation(@RequestBody Parametre jsonParameter) {
        Parametre param = parametreDao.findById(jsonParameter.getId());
        param.setValue(jsonParameter.getValue());
        parametreDao.save(param);

        return new SuccessResponse("ok");
    }

}
