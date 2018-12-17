package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.EtatImproDto;
import com.bparent.improPhoto.dto.InfoDto;
import com.bparent.improPhoto.dto.StatutPreparationDto;
import com.bparent.improPhoto.dto.VersioningDto;
import com.bparent.improPhoto.exception.ImproControllerException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.EtatImproService;
import com.bparent.improPhoto.service.StatutPreparationService;
import com.bparent.improPhoto.service.VersionService;
import com.bparent.improPhoto.util.NetworkUtils;
import com.bparent.improPhoto.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class MainAjaxController {

    @Autowired
    private EtatImproService etatImproService;

    @Autowired
    private StatutPreparationService statutPreparationService;

    @Autowired
    private VersionService versionService;

    @Value("${application.name}")
    private String applicationName;

    @Value("${build.version}")
    private String buildVersion;

    @Value("${build.timestamp}")
    private String buildTimestamp;

    @Value("${server.port}")
    private String serverPort;


    @GetMapping("/statutPreparation")
    public StatutPreparationDto getStatutPreparation() throws ImproControllerException {
        try {
            return statutPreparationService.getStatutPreparation();
        } catch (ImproServiceException e) {
            throw new ImproControllerException("Error while getting statut preparation", e);
        }
    }

    @GetMapping("/etatImpro")
    public EtatImproDto getEtatImpro() {
        return etatImproService.getStatut();
    }

    @GetMapping("/applicationInfo")
    public InfoDto getInfos() {
        return InfoDto.builder()
                .ip(NetworkUtils.getIpString(serverPort))
                .applicationName(applicationName)
                .applicationVersion(buildVersion)
                .applicationTimestamp(buildTimestamp)
                .build();
    }

    @GetMapping("/versioning")
    public VersioningDto versioning() {
        return VersioningDto.builder()
                .versions(versionService.findAll())
                .build();
    }

    @PostMapping("/restart")
    public void restartRaspberry() {
        log.debug("Restart raspberry");
        final String cmd = "sudo reboot";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(StringUtils.stackTrace(e));
        }
    }

    @PostMapping("/shutdown")
    public void shutdownRaspberry() {
        log.debug("Shutdown raspberry");
        final String cmd = "sudo shutdown -h now";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(StringUtils.stackTrace(e));
        }
    }

}
