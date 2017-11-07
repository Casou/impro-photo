package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TestWsController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public BasicCodeLabelDto greeting(BasicCodeLabelDto message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new BasicCodeLabelDto(message.getCode(), "Hello, " + message.getLabel() + "!");
    }

}