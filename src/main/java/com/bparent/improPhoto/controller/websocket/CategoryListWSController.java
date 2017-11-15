package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryListWSController {

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

}
