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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
public class MusiquesAjaxController {

    private static final Predicate<String> isAudioFileAccepted = fileExtension -> IConstants.ZIP_EXTENSION.equals(fileExtension)
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
        request.getFiles("file").forEach(this::copyFile);

        return new SuccessResponse("ok");
    }

    private void copyFile(MultipartFile multipart) {
        String originalFilename = multipart.getOriginalFilename();
        String fileExtension = FileUtils.getFileExtension(originalFilename);

        if (!isAudioFileAccepted.test(fileExtension)) {
            throw new IllegalArgumentException("Ce type de fichier n'est pas autorisé : " + fileExtension);
        }

        if (IConstants.ZIP_EXTENSION.equals(fileExtension)) {
            try {
                this.unzipFile(multipart, originalFilename);
            } catch (IOException e) {
                throw new RejectedExecutionException("Error while unzip file", e);
            }
        } else {
            this.copyAudioFile(multipart, originalFilename, IConstants.IPath.IAudio.AUDIOS_PLAYLIST);
        }
    }

    private File copyAudioFile(MultipartFile multipart, String originalFilename, String folderName) {
        File copiedFile = new File(folderName + originalFilename);
        FileOutputStream fos = null;
        try {
            copiedFile.createNewFile();

            fos = new FileOutputStream(copiedFile);
            fos.write(multipart.getBytes());

        } catch (IOException e) {
            throw new RejectedExecutionException("Error while copying file", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println("Error while copying file (closing FileOutputStream");
                    e.printStackTrace();
                }

            }
        }

        return copiedFile;
    }

    private void unzipFile(MultipartFile multipart, String fileZip) throws IOException {
        File tempZipFile = this.copyAudioFile(multipart, fileZip, IConstants.IPath.MEDIAS_TEMP);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            if (!IConstants.AUDIO_EXTENSION_ACCEPTED.contains(FileUtils.getFileExtension(fileName))) {
                zipEntry = zis.getNextEntry();
                continue;
            }
            fileName = FileUtils.getFileName(fileName);
            File newFile = new File(IConstants.IPath.IAudio.AUDIOS_PLAYLIST + fileName);
            //create directories for sub directories in zip
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        if (!tempZipFile.delete()) {
            throw new RejectedExecutionException("Error while deleting temp zip file");
        }
    }

}
