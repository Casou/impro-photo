package com.bparent.improPhoto.controller.websocket;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.dto.MusiqueDto;
import com.bparent.improPhoto.dto.VolumeDto;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ControlPanelWSController {

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

    @MessageMapping("/action/playPlaylist")
    @SendTo("/topic/general/playPlaylist")
    public BasicCodeLabelDto playPlaylist() {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/pausePlaylist")
    @SendTo("/topic/general/pausePlaylist")
    public BasicCodeLabelDto pausePlaylist() {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/playlistPlaying")
    @SendTo("/topic/general/playlistPlaying")
    public MusiqueDto playlistPlaying(MusiqueDto songDto) {
        etatImproService.updateStatus(IConstants.IEtatImproField.CURRENT_SONG, songDto.getId() + "");
        etatImproService.updateStatus(IConstants.IEtatImproField.PLAYLIST_STATUS, IConstants.IEtatImproField.IStatutPlaylist.PLAYING);
        return songDto;
    }

    @MessageMapping("/action/playlistPaused")
    @SendTo("/topic/general/playlistPaused")
    public BasicCodeLabelDto playlistPaused() {
        etatImproService.updateStatus(IConstants.IEtatImproField.PLAYLIST_STATUS, IConstants.IEtatImproField.IStatutPlaylist.PAUSED);
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/updateSong")
    @SendTo("/topic/general/updateSong")
    public MusiqueDto updateSong(MusiqueDto songDto) {
        etatImproService.updateStatus(IConstants.IEtatImproField.CURRENT_SONG, songDto.getId() + "");
        return songDto;
    }

    @MessageMapping("/action/nextSong")
    @SendTo("/topic/general/nextSong")
    public BasicCodeLabelDto nextSong() {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/setVolume")
    @SendTo("/topic/general/setVolume")
    public VolumeDto setVolume(VolumeDto volumeDto) {
        etatImproService.updateStatus(IConstants.IEtatImproField.PLAYLIST_VOLUME, String.valueOf(volumeDto.getVolume()));
        return volumeDto;
    }

    @MessageMapping("/action/playJingle")
    @SendTo("/topic/general/playJingle")
    public MusiqueDto playJingle(MusiqueDto songDto) {
        return songDto;
    }

    @MessageMapping("/action/stopJingle")
    @SendTo("/topic/general/stopJingle")
    public BasicCodeLabelDto stopJingle() {
        return new BasicCodeLabelDto("message", "ok");
    }

    @MessageMapping("/action/jingleStopped")
    @SendTo("/topic/general/jingleStopped")
    public BasicCodeLabelDto jingleStopped() {
        return new BasicCodeLabelDto("message", "ok");
    }

}