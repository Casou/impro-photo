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
public class WebSocketController {

    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/resetImpro")
    @SendTo("/topic/general/refresh")
    public BasicCodeLabelDto resetImpro() {
        etatImproService.resetImpro();
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/refresh")
    @SendTo("/topic/general/refresh")
    public BasicCodeLabelDto refresh() {
        return new BasicCodeLabelDto("message", "ok");
    }

}