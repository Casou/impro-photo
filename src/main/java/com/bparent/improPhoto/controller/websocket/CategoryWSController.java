package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.json.PictureDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
public class CategoryWSController {

    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/category/selectPicture")
    @SendTo("/topic/category/selectPicture")
    public PictureDto selectPicture(PictureDto pictureDto) {
        EtatImproDto statut = etatImproService.getStatut();
        statut.getPhotosChoisies().add(pictureDto.getId());
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, statut.getPhotosChoisies());
        etatImproService.updateStatus(IConstants.IEtatImproField.INTEGRALITE, (String) null);

        return pictureDto;
    }

    @MessageMapping("/action/category/unselectPicture")
    @SendTo("/topic/category/unselectPicture")
    public PictureDto unselectPicture(PictureDto pictureDto) {
        EtatImproDto statut = etatImproService.getStatut();
        statut.setPhotosChoisies(statut.getPhotosChoisies().stream()
            .filter(id -> id != pictureDto.getId())
            .collect(Collectors.toList()));
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, statut.getPhotosChoisies());

        return pictureDto;
    }

    @MessageMapping("/action/category/validateSelection")
    @SendTo("/topic/category/validateSelection")
    public BasicCodeLabelDto validateSelection() {
        etatImproService.updateStatus(IConstants.IEtatImproField.STATUT_DIAPO, IConstants.IEtatImproField.IStatutDiapo.LAUNCHED);
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTO_COURANTE, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.BLOCK_MASQUES, new ArrayList<>());
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/category/cancelSelection")
    @SendTo("/topic/category/cancelSelection")
    public BasicCodeLabelDto cancelSelection() {
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, new ArrayList<>());
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTO_COURANTE, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.INTEGRALITE, String.valueOf(false));
        etatImproService.updateStatus(IConstants.IEtatImproField.STATUT_DIAPO, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.BLOCK_MASQUES, new ArrayList<>());
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/category/selectAll")
    @SendTo("/topic/category/selectAll")
    public BasicCodeLabelDto selectAll() {
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, new ArrayList<>());
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTO_COURANTE, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.INTEGRALITE, String.valueOf(true));
        etatImproService.updateStatus(IConstants.IEtatImproField.STATUT_DIAPO, (String) null);
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/category/showPicture")
    @SendTo("/topic/category/showPicture")
    public PictureDto showPicture(PictureDto pictureDto) {
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTO_COURANTE, pictureDto.getId().toString());
        return pictureDto;
    }

    @MessageMapping("/action/category/backToBlack")
    @SendTo("/topic/category/backToBlack")
    public BasicCodeLabelDto backToBlack() {
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTO_COURANTE, (String) null);
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/category/returnToList")
    @SendTo("/topic/category/returnToList")
    public BasicCodeLabelDto returnToList() {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, "CATEGORIES");
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTOS_CHOISIES, new ArrayList<>());
        etatImproService.updateStatus(IConstants.IEtatImproField.PHOTO_COURANTE, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.INTEGRALITE, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.STATUT_DIAPO, (String) null);
        etatImproService.updateStatus(IConstants.IEtatImproField.BLOCK_MASQUES, new ArrayList<>());
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/playAppareilPhoto")
    @SendTo("/topic/playAppareilPhoto")
    public BasicCodeLabelDto playAppareilPhoto() {
        return new BasicCodeLabelDto("message", "ok");
    }

}
