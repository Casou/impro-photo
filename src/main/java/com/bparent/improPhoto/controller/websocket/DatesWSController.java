package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.json.NewScreenDto;
import com.bparent.improPhoto.dto.json.PictureDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DatesWSController {

    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/dates/goIntro")
    @SendTo("/topic/dates/goIntro")
    public BasicCodeLabelDto goIntro(NewScreenDto newScreen) {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, newScreen.getNewScreen());
        return new BasicCodeLabelDto("message", "ok");
    }

}
