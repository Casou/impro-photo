package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.CategorieDto;
import com.bparent.improPhoto.dto.json.NewScreenDto;
import com.bparent.improPhoto.dto.json.PictureDto;
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
public class RemerciementsWSController {

    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/remerciements/goDates")
    @SendTo("/topic/remerciements/goDates")
    public BasicCodeLabelDto goDates(NewScreenDto newScreen) {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, newScreen.getNewScreen());
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/remerciements/showPicture")
    @SendTo("/topic/remerciements/showPicture")
    public PictureDto showPicture(PictureDto picture) {
        return picture;
    }

    @MessageMapping("/action/remerciements/showText")
    @SendTo("/topic/remerciements/showText")
    public BasicCodeLabelDto showText() {
        return new BasicCodeLabelDto("message", "ok");
    }

}
