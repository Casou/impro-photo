package com.bparent.improPhoto;

import com.bparent.improPhoto.dao.ParametreDao;
import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.domain.ParametrePK;
import com.bparent.improPhoto.dto.ParametreDto;
import com.bparent.improPhoto.enums.DefaultParameters;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.service.ParametreService;
import com.bparent.improPhoto.util.AsciiArtUtils;
import com.bparent.improPhoto.util.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class StartupCheckRunner implements CommandLineRunner {

    @Autowired
    private ParametreService parametreService;

    @Autowired
    private ParametreDao parametreDao;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public void run(String... strings) throws Exception {
        checkDefaultParameters();

        // System.out.println(NetworkUtils.getFormattedIpString(environment.getProperty("server.serverPort")));
        AsciiArtUtils.printMessage(NetworkUtils.getIpString(serverPort));
        System.out.println("\n\n\n\n\n\n\n");
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

}
