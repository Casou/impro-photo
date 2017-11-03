package com.bparent.improPhoto.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    public static String formatPathWithCharacter(String path, String replaceChar) {
        String replacement = replaceChar;
        if (replacement.equals("\\")) {
            replacement = "\\\\";
        }
        return path.replaceAll("\\\\", replacement)
                .replaceAll("/", replacement);
    }

    public static String formatPathToSystemDefault(String path) {
        return FileUtils.formatPathWithCharacter(path, File.separator);
    }

    public static List<String> listAllFilesRecursivly(String folder) throws IOException {
        return Files.walk(Paths.get(folder))
                .filter(Files::isRegularFile)
                .map(pathObject -> pathObject.toFile().getPath())
//                .filter(path -> excludeFolders.stream()
//                        .filter(excluded -> !path.contains(excluded + File.separator))
//                        .findAny()
//                        .isPresent())
                .collect(Collectors.toList());
    }

}
