package com.bparent.improPhoto.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
public class FileUtils {

    public static String getFileExtension(String filePath) {
        final File f = new File(filePath);
        final String fileName = f.getName();

        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        } else {
            return "";
        }
    }

    public static String getFileNameWithoutExtension(String fileName) {
        if(fileName.lastIndexOf(".") >= 0)
            return fileName.substring(0, fileName.lastIndexOf("."));
        else return fileName;
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

    public static String getFrontFilePath(File file) {
        return FileUtils.formatPathWithCharacter("/handler/" + file.getPath(), "/");
    }

    public static String getFileName(String filePath) {
        return new File(filePath).getName();
    }

    public static String getFolderName(String filePath) {
        String trimedFilePath = filePath;
        if (filePath.endsWith("/")) {
            trimedFilePath = trimedFilePath.substring(0, filePath.length() - 1);
        }

        final int lastIndex = trimedFilePath.lastIndexOf("/");
        if (lastIndex <= 0) {
            return "";
        }
        return trimedFilePath.substring(0, lastIndex) + "/";
    }

    public static void deleteFolder(File folder) {
        deleteFolderContent(folder);
        folder.delete();
    }

    public static void deleteFolderContent(File folder) {
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder " + folder.getAbsolutePath() + " not found");
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("File " + folder.getAbsolutePath() + " is not a folder");
        }

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                FileUtils.deleteFolder(file);
            } else {
                FileUtils.deleteFile(file);
            }
        }
    }

    public static void deleteFile(File f) {
        if (!f.exists()) {
            throw new IllegalArgumentException("Le fichier " + f.getName() + " n'existe pas.");
        }

        if (!f.delete()) {
            throw new RejectedExecutionException("Une erreur est survenue lors de la suppression du fichier : " + f.getName());
        }
    }

    public static String capitalizeCategoryFolderName(String folderName) {
        String capitalizedString = folderName;
        char c = capitalizedString.charAt(0);
        while (!isCharacterToRemoveFromCategoryFolder(c)) {
            capitalizedString = capitalizedString.substring(1);
            c = capitalizedString.charAt(0);
        }

        return StringUtils.capitalize(capitalizedString.trim());
    }

    private static final String ACCEPTED_CHARS_FOR_FOLDER_NAME =
            "abcdefghijqklmnopqrstuvwxyz" +
            "ABCDEFGHIJQKLMNOPQRSTUVWXYZ" +
            "aàâäéèêëiîïoôuùû" +
            "AÀÂÄÉÈÊËIÎÏOÔUÙÛ";
    private static boolean isCharacterToRemoveFromCategoryFolder(char c) {
        return ACCEPTED_CHARS_FOR_FOLDER_NAME.contains(c + "");
    }

    public static String sanitizeFilename(String inputName) {
        return inputName.replaceAll("[^/a-zA-Z0-9-_\\.]", "_");
    }

}
