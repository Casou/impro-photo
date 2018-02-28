package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.json.NewScreenDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class CategoryListWSController {

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private EtatImproService etatImproService;


    @MessageMapping("/action/showNextCategory")
    @SendTo("/topic/category_list/showNextCategory")
    public BasicCodeLabelDto showNextCategory() {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/showAllCategories")
    @SendTo("/topic/category_list/showAllCategories")
    public BasicCodeLabelDto showAllCategories() {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/launchCategorie")
    @SendTo("/topic/category_list/launchCategorie")
    public CategorieDto launchCategorie(CategorieDto categorie) throws ImproMappingException {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, "CATEGORY");
        etatImproService.updateStatus(IConstants.IEtatImproField.ID_CATEGORIE, categorie.getId() + "");
        etatImproService.updateStatus(IConstants.IEtatImproField.BLOCK_MASQUES, new ArrayList<>());

        CategorieDto dto = categorieService.findById(categorie.getId());
        dto.setTermine(true);
        this.categorieService.save(dto);

        etatImproService.updateStatus(IConstants.IEtatImproField.TYPE_ECRAN, dto.getType().getCode());
        return dto;
    }

    @MessageMapping("/action/goRemerciements")
    @SendTo("/topic/category_list/goRemerciements")
    public BasicCodeLabelDto goRemerciements(NewScreenDto newScreen) {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, newScreen.getNewScreen());
        return new BasicCodeLabelDto("message", "ok");
    }

}
