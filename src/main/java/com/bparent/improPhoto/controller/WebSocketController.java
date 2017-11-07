package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
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

}