package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Controller
public class CategoryWSController {


    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/category/selectPicture")
    @SendTo("/topic/category/selectPicture")
    public CategorieDto selectPicture(CategorieDto categorieDto) {
        EtatImproDto statut = etatImproService.getStatut();
        statut.getPhotosChoisies().add(categorieDto.getId());
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, statut.getPhotosChoisies());

        return categorieDto;
    }

    @MessageMapping("/action/category/unselectPicture")
    @SendTo("/topic/category/unselectPicture")
    public CategorieDto unselectPicture(CategorieDto categorieDto) {
        EtatImproDto statut = etatImproService.getStatut();
        statut.setPhotosChoisies(statut.getPhotosChoisies().stream()
            .filter(id -> id != categorieDto.getId())
            .collect(Collectors.toList()));
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, statut.getPhotosChoisies());

        return categorieDto;
    }

    @MessageMapping("/action/category/validateSelection")
    @SendTo("/topic/category/validateSelection")
    public BasicCodeLabelDto validateSelection() {
        etatImproService.updateStatus(IConstants.IEtatImproField.STATUT_DIAPO, IConstants.IEtatImproField.IStatutDiapo.LAUNCHED);
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/category/cancelSelection")
    @SendTo("/topic/category/cancelSelection")
    public BasicCodeLabelDto cancelSelection() {
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.STATUT_DIAPO, (String) null);
        return new BasicCodeLabelDto("message", "ok");
    }

}