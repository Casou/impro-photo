package com.bparent.improPhoto.service;

import com.bparent.improPhoto.exception.ImproServiceException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RemerciementService {

    public static final String REMERCIEMENTS_FILEPATH = "datas/remerciements.txt";

    public String getRemerciements() throws ImproServiceException {
        File file = new File(REMERCIEMENTS_FILEPATH);
        if (!file.exists()) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new ImproServiceException("Error while reading remerciements", e);
        }
    }

    public void saveRemerciement(String remerciement) throws ImproServiceException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(REMERCIEMENTS_FILEPATH, "UTF-8");
            writer.print(remerciement);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new ImproServiceException("Error while writing remerciements", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
