package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.json.ActionDto;
import com.bparent.improPhoto.dto.json.MessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    @MessageMapping("/action/launchImpro")
    @SendTo("/topic/salle_attente/launchImpro")
    public BasicCodeLabelDto launchImpro() throws Exception {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/toggleAnimation")
    @SendTo("/topic/salle_attente/toggleAnimation")
    public MessageResponse toggleAnimation(ActionDto isAnimationOn) throws Exception {
        return new MessageResponse(isAnimationOn.getAction());
    }

}