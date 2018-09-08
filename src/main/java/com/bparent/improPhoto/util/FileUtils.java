package com.bparent.improPhoto.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
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

    public static String getFrontFilePath(File file) {
        return FileUtils.formatPathWithCharacter("/handler/" + file.getPath(), "/");
    }

    public static String getFileName(String filePath) {
        return new File(filePath).getName();
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
                file.delete();
            }
        }
    }

    public static void handleUploadedFile(MultipartFile multipart, Predicate<String> isAcceptedFile,
                                          List<String> fileExtensionAccepted, String destinationFolder,
                                          boolean flatPath) {
        String originalFilename = multipart.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        if (!isAcceptedFile.test(fileExtension)) {
            throw new IllegalArgumentException("Ce type de fichier n'est pas autorisé : " + fileExtension);
        }

        if (IConstants.ZIP_EXTENSION.equals(fileExtension)) {
            try {
                unzipUploadedFile(multipart, originalFilename, fileExtensionAccepted, destinationFolder, flatPath);
            } catch (IOException e) {
                throw new RejectedExecutionException("Error while unzip file", e);
            }
        } else {
            copyUploadedFile(multipart, originalFilename, destinationFolder);
        }
    }

    public static File copyUploadedFile(MultipartFile multipart, String newFilename, String folderName) {
        File copiedFile = new File(folderName + newFilename);
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

    private static void unzipUploadedFile(MultipartFile multipart, String fileZip, List<String> fileExtensionsAccepted,
                                          String destinationFolder, boolean flatPath) throws IOException {
        File tempZipFile = copyUploadedFile(multipart, fileZip, IConstants.IPath.MEDIAS_TEMP);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            if (!fileExtensionsAccepted.contains(getFileExtension(fileName))) {
                zipEntry = zis.getNextEntry();
                continue;
            }
            if (flatPath) {
                fileName = FileUtils.getFileName(fileName);
            }
            File newFile = new File(destinationFolder + fileName);
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
}
