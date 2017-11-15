package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.CategorieListDto;
import com.bparent.improPhoto.dto.json.MessageJson;
import com.bparent.improPhoto.dto.json.NewScreenDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.service.CategorieService;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class IntroWSController {

    @Autowired
    private EtatImproService etatImproService;

    @Autowired
    private CategorieService categorieService;


    @MessageMapping("/action/intro/playVideo")
    @SendTo("/topic/intro/playVideo")
    public BasicCodeLabelDto playVideo(MessageJson messageJson) {
        return new BasicCodeLabelDto("video", messageJson.message);
    }

    @MessageMapping("/action/intro/pauseVideo")
    @SendTo("/topic/intro/pauseVideo")
    public BasicCodeLabelDto pauseVideo(MessageJson messageJson) {
        return new BasicCodeLabelDto("video", messageJson.message);
    }

    @MessageMapping("/action/intro/stopVideo")
    @SendTo("/topic/intro/stopVideo")
    public BasicCodeLabelDto stopVideo(MessageJson messageJson) {
        return new BasicCodeLabelDto("video", messageJson.message);
    }

    @MessageMapping("/action/intro/goCategories")
    @SendTo("/topic/intro/goCategories")
    public CategorieListDto goCategories(NewScreenDto newScreen) throws ImproMappingException {
        etatImproService.updateStatus(IConstants.IEtatImproField.ECRAN, newScreen.getNewScreen());
        return new CategorieListDto(this.categorieService.findAll());
    }

}