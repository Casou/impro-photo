package com.bparent.improPhoto;

import com.bparent.improPhoto.dao.ParametreDao;
import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.domain.ParametrePK;
import com.bparent.improPhoto.dto.ParametreDto;
import com.bparent.improPhoto.enums.DefaultParameters;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.ParametreService;
import com.bparent.improPhoto.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class StartupCheckRunner implements CommandLineRunner {

    @Autowired
    private ParametreService parametreService;

    @Autowired
    private ParametreDao parametreDao;

    @Value("${server.port}")
    private String serverPort;

    @Value("${chrome.path}")
    private String chromePath;

    public static boolean IS_RASPBERRY;

    @Override
    public void run(String... args) throws Exception {
        this.checkDefaultParameters();
        this.sanitizeFiles();

        // System.out.println(NetworkUtils.getFormattedIpString(environment.getProperty("server.serverPort")));
        // AsciiArtUtils.printMessage(NetworkUtils.getIpString(serverPort));
        log.info("********** Application launched **********\n\n\n");
        log.info("IP : " + NetworkUtils.getIpString(serverPort) + "\n\n\n");


        IS_RASPBERRY = Arrays.asList(args).contains("-raspberry");
        if (Arrays.asList(args).contains("-browser")) {
            this.launchBrowser();
        }
    }

    private void checkDefaultParameters() throws ImproServiceException {
        List<ParametreDto> parametres = parametreService.findAll();
        for (DefaultParameters defaultParameters : DefaultParameters.values()) {
            if (!parametres.stream().anyMatch(parametreDto -> parametreDto.equals(defaultParameters.getParametre()))) {
                parametreDao.save(Parametre.builder()
                        .id(new ParametrePK(defaultParameters.getParametre().getContext(), defaultParameters.getParametre().getKey()))
                        .value(defaultParameters.getParametre().getValue())
                        .valueType(defaultParameters.getParametre().getValueType())
                        .description("Valeur par défaut du paramètre (insérée lors du startup)")
                        .build());
            }
        }
    }

    private void launchBrowser() {
        log.debug("Try to launch Chrome");
        final String cmd = chromePath + " -kiosk -fullscreen " + NetworkUtils.getIpString(serverPort);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(StringUtils.stackTrace(e));
        }
    }

    private void sanitizeFiles() {
        log.info("***** Sanitize all files *****\n\n");

        this.sanitizeFolder(IConstants.IPath.IAudio.AUDIOS);
        this.sanitizeFolder(IConstants.IPath.IPhoto.PHOTOS);
        this.sanitizeFolder(IConstants.IPath.IVideo.VIDEOS);

        log.info("***** Sanitize DONE *****\n\n");
    }

    private void sanitizeFolder(String folderPath) {
        File playlistFolder = new File(folderPath);
        log.info("Sanitize folder " + folderPath);

        Arrays.stream(Objects.requireNonNull(playlistFolder.listFiles())).forEach(file -> {
            if (file.isDirectory()) {
                this.sanitizeFolder(file.getAbsolutePath());
                return;
            }

            if (!file.isFile()) {
                return;
            }

            final String newFilePath = folderPath + "/" + FileUtils.sanitizeFilename(file.getName());
            final String originAbsolutePath = file.getAbsolutePath();
            log.debug("> Sanitize file : " + originAbsolutePath + " >> " + newFilePath);

            if (!file.renameTo(new File(newFilePath))) {
                log.error(">>> Error while renaming file " + originAbsolutePath + " into " + newFilePath);
            }
        });
    }

}
