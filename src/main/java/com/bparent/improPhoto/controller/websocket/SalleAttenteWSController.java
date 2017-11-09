package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.json.ActionDto;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.NewScreenDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SalleAttenteWSController {

    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/launchImpro")
    @SendTo("/topic/salle_attente/launchImpro")
    public BasicCodeLabelDto launchImpro(NewScreenDto newScreen) {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, newScreen.getNewScreen());
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/toggleAnimation")
    @SendTo("/topic/salle_attente/toggleAnimation")
    public MessageResponse toggleAnimation(ActionDto isAnimationOn) {
        return new MessageResponse(isAnimationOn.getAction());
    }

}