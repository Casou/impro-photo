package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.json.MaskDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PolaroidWSController {

    @Autowired
    private EtatImproService etatImproService;

    @MessageMapping("/action/polaroid/hideMask")
    @SendTo("/topic/polaroid/hideMask")
    public MaskDto hideMask(MaskDto mask) {

        EtatImproDto statut = etatImproService.getStatut();
        statut.getBlockMasques().add(mask.getId());
        etatImproService.updateStatus(IConstants.IEtatImproField.BLOCK_MASQUES, statut.getBlockMasques());

        return mask;
    }

}
