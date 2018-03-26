package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.SongDto;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;

@RestController
public class MusiquesAjaxController {

    private static final Predicate<String> isAcceptedSongFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension)
            || IConstants.AUDIO_EXTENSION_ACCEPTED.contains(fileExtension);

    @DeleteMapping(value = "/song", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteSongRequest(@RequestBody SongDto songDto) {
        this.deleteSong(songDto);

        return new SuccessResponse("ok");
    }

    private void deleteSong(@RequestBody SongDto songDto) {
        File f = new File(IConstants.IPath.IAudio.AUDIOS_PLAYLIST + songDto.getNom());
        if (!f.exists()) {
            throw new IllegalArgumentException("Le fichier " + songDto.getNom() + " n'existe pas.");
        }

        if (!f.delete()) {
            throw new RejectedExecutionException("Une erreur est survenue lors de la suppression du fichier : " + songDto.getNom());
        }
    }

    @DeleteMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteSongListRequest(@RequestBody List<SongDto> songDtoList) {
        songDtoList.forEach(this::deleteSong);

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadSongs(MultipartHttpServletRequest request,
                                                                     HttpServletResponse response) {
        request.getFiles("file").forEach(multipart -> FileUtils.handleUploadedFile(multipart, isAcceptedSongFile,
                IConstants.AUDIO_EXTENSION_ACCEPTED, IConstants.IPath.IAudio.AUDIOS_PLAYLIST, true));

        return new SuccessResponse("ok");
    }

}
