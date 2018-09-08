package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.JingleCategoryDto;
import com.bparent.improPhoto.dto.JingleDto;
import com.bparent.improPhoto.dto.SongDto;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.service.JingleService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JingleService jingleService;

    private static final Predicate<String> isAcceptedSongFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension)
            || IConstants.AUDIO_EXTENSION_ACCEPTED.contains(fileExtension);

    @DeleteMapping(value = "/song", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteSongRequest(@RequestBody SongDto songDto) {
        this.deleteSong(IConstants.IPath.IAudio.AUDIOS_PLAYLIST, songDto.getNom());

        return new SuccessResponse("ok");
    }

    private void deleteSong(String path, String nom) {
        File f = new File(path + nom);
        if (!f.exists()) {
            throw new IllegalArgumentException("Le fichier " + nom + " n'existe pas.");
        }

        if (!f.delete()) {
            throw new RejectedExecutionException("Une erreur est survenue lors de la suppression du fichier : " + nom);
        }
    }

    @DeleteMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteSongList(@RequestBody List<SongDto> songDtoList) {
        songDtoList.forEach(songDto -> this.deleteSong(IConstants.IPath.IAudio.AUDIOS_PLAYLIST, songDto.getNom()));

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadSongs(MultipartHttpServletRequest request,
                                                                     HttpServletResponse response) {
        request.getFiles("file").forEach(multipart -> FileUtils.handleUploadedFile(multipart, isAcceptedSongFile,
                IConstants.AUDIO_EXTENSION_ACCEPTED, IConstants.IPath.IAudio.AUDIOS_PLAYLIST, true));

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/jingle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteJingle(@RequestBody JingleDto jingleDto) {
        this.deleteSong(IConstants.IPath.IAudio.AUDIOS_JINGLES + jingleDto.getFolder() + "/", jingleDto.getNom());

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/jingleCategory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteJingleCategory(@RequestBody JingleCategoryDto jingleCategoryDto) {
        this.jingleService.deleteJingleCategory(jingleCategoryDto);

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/jingles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadJingles(MultipartHttpServletRequest request,
                                                                     HttpServletResponse response) {
        request.getFiles("file").forEach(multipart -> FileUtils.handleUploadedFile(multipart, isAcceptedSongFile,
                IConstants.AUDIO_EXTENSION_ACCEPTED, IConstants.IPath.IAudio.AUDIOS_JINGLES, false));

        return new SuccessResponse("ok");
    }

    @PostMapping(value = "/jingleCategory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadJinglesIntoCategory(MultipartHttpServletRequest request,
                                                                     HttpServletResponse response) {
        request.getFiles("file").forEach(multipart -> FileUtils.handleUploadedFile(multipart, isAcceptedSongFile,
                IConstants.AUDIO_EXTENSION_ACCEPTED,
                IConstants.IPath.IAudio.AUDIOS_JINGLES + request.getParameter("category_name") + "/",
                true));

        return new SuccessResponse("ok");
    }

}
