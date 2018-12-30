package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.MusiqueDao;
import com.bparent.improPhoto.domain.Musique;
import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.MusiqueDto;
import com.bparent.improPhoto.dto.UploadedFileDto;
import com.bparent.improPhoto.dto.json.ErrorResponse;
import com.bparent.improPhoto.dto.json.MessageResponse;
import com.bparent.improPhoto.dto.json.SuccessResponse;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.service.JingleService;
import com.bparent.improPhoto.util.FileUtils;
import com.bparent.improPhoto.util.IConstants;
import com.bparent.improPhoto.util.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class MusiqueAjaxController {

    @Autowired
    private JingleService jingleService;

    @Autowired
    private MusiqueDao musiqueDao;

    @Autowired
    private EtatImproService etatImproService;

    protected static final Predicate<String> isAcceptedSongFile = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension)
            || IConstants.AUDIO_EXTENSION_ACCEPTED.contains(fileExtension);

    @GetMapping("/songs")
    public List<MusiqueDto> getAllPlaylistSongs() {
        return musiqueDao.findAll().stream()
                .map(MusiqueDto::new)
                . collect(Collectors.toList());
    }

    @PostMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> uploadSongs(MultipartHttpServletRequest request,
                                                                     HttpServletResponse response) {
        List<UploadedFileDto> uploadedFileDtos = request.getFiles("file").stream().map(multipart ->
                ZipUtils.copyAnyUploadedFile(multipart, isAcceptedSongFile, IConstants.AUDIO_EXTENSION_ACCEPTED,
                        IConstants.IPath.IAudio.AUDIOS_PLAYLIST, true))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        musiqueDao.save(uploadedFileDtos.stream().map(UploadedFileDto::toMusique).collect(Collectors.toList()));

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/song/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteSongRequest(@PathVariable("id") BigInteger id) {
        Musique musique = musiqueDao.findOne(id);
        if (musique == null) {
            return new ErrorResponse("Musique " + id + " not found");
        }

        FileUtils.deleteFile(new File(IConstants.IPath.IAudio.AUDIOS_PLAYLIST + musique.getFileName()));
        musiqueDao.delete(musique);

        EtatImproDto statut = etatImproService.getStatut();
        if (id.equals(statut.getCurrentSongId())) {
            etatImproService.updateStatus(IConstants.IEtatImproField.CURRENT_SONG, (String) null);
        }

        return new SuccessResponse("ok");
    }

    @DeleteMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<MessageResponse> deleteSongList(@RequestBody List<MusiqueDto> songDtoList) {
        Optional<ResponseEntity<MessageResponse>> optErrorResponse = songDtoList.stream()
                .map(songDto -> deleteSongRequest(songDto.getId()))
                .filter(responseEntity -> responseEntity instanceof ErrorResponse)
                .findFirst();

        return optErrorResponse
                .orElse(new SuccessResponse("ok"));
    }

}
