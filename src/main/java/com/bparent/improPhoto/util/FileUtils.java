package com.bparent.improPhoto.util;

import com.bparent.improPhoto.dto.UploadedFileDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class FileUtils {

    public static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") > 0)
            return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        else return "";
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

    public static List<UploadedFileDto> handleUploadedFile(MultipartFile multipart, Predicate<String> isAcceptedFile,
                                                           List<String> fileExtensionAccepted, String destinationFolder,
                                                           boolean flatPath) {
        String originalFilename = multipart.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        log.debug("Uploaded file : " + originalFilename);

        if (!isAcceptedFile.test(fileExtension)) {
            throw new IllegalArgumentException("Ce type de fichier n'est pas autorisé : " + fileExtension);
        }

        if (IConstants.ZIP_EXTENSION.equals(fileExtension)) {
            try {
                return unzipUploadedFile(multipart, originalFilename, fileExtensionAccepted, destinationFolder, flatPath);
            } catch (IOException e) {
                throw new RejectedExecutionException("Error while unzip file", e);
            }
        } else {
            return Arrays.asList(copyUploadedFile(multipart, originalFilename, destinationFolder));
        }
    }

    public static UploadedFileDto copyUploadedFile(MultipartFile multipart, String originalFilename, String destinationFolder) {
        log.debug("Copy file : " + originalFilename);
        final String destinationFilePath = getUniqueFilePath(destinationFolder + FileUtils.sanitizeFilename(originalFilename));
        File copiedFile = new File(destinationFilePath);
        FileOutputStream fos = null;
        try {
            copiedFile.createNewFile();

            fos = new FileOutputStream(copiedFile);
            fos.write(multipart.getBytes());

            log.debug("File copied : " + originalFilename);

            return UploadedFileDto.builder()
                    .copiedFile(copiedFile)
                    .fileName(FileUtils.getFileName(destinationFilePath))
                    .name(originalFilename)
                    .build();

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
    }

    private static String getUniqueFilePath(String filePath) {
        String uniqueFilePath = filePath;
        File f = new File(uniqueFilePath);
        int cpt = 1;
        while (f.exists()) {
            uniqueFilePath = getFileNameWithoutExtension(filePath) + "_" + cpt + "." + getFileExtension(filePath);
            f = new File(uniqueFilePath);
            cpt++;
        }
        return uniqueFilePath;
    }

    private static List<UploadedFileDto> unzipUploadedFile(MultipartFile multipart, String fileZip, List<String> fileExtensionsAccepted,
                                          String destinationFolder, boolean flatPath) throws IOException {
        log.debug("Unzip file...");
        final List<UploadedFileDto> uploadedFiles = new ArrayList<>();

        final UploadedFileDto uploadedFileDto = copyUploadedFile(multipart, fileZip, IConstants.IPath.MEDIAS_TEMP);
        final File tempZipFile = uploadedFileDto.getCopiedFile();

        int b;
        byte[] buffer = new byte[1024];

        final ZipFile zipFile = new ZipFile(tempZipFile, CharsetDetector.detectCharset(tempZipFile));
        final Enumeration e = zipFile.entries();

        while (e.hasMoreElements()) {
            final ZipEntry entry = (ZipEntry) e.nextElement();

            if (entry.isDirectory()) {
                log.debug("\t\tDirectory : " + entry.getName());
                continue;
            }

            String filePath = entry.getName();

            if (!fileExtensionsAccepted.contains(getFileExtension(filePath))) {
                log.info("\t\tFile : " + filePath + " (rejected)");
                continue;
            }

            log.debug("\t\tFile : " + filePath);
            final String fileName = FileUtils.getFileName(filePath);
            if (flatPath) {
                filePath = fileName;
            }

            final String destinationFilePath = destinationFolder +
                    Arrays.stream(filePath.split("/"))
                            .map(FileUtils::sanitizeFilename)
                            .collect(Collectors.joining("/"));
            final File destinationFile = new File(destinationFilePath);

            //create directories for sub directories in zip
            new File(destinationFile.getParent()).mkdirs();

            // Copy file
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(zipFile.getInputStream(entry));

                final FileOutputStream fos = new FileOutputStream(destinationFile);
                bos = new BufferedOutputStream(fos, 1024);

                while ((b = bis.read(buffer, 0, 1024)) != -1) {
                    bos.write(buffer, 0, b);
                }

                uploadedFiles.add(UploadedFileDto.builder()
                        .copiedFile(destinationFile)
                        .fileName(FileUtils.getFileName(destinationFilePath))
                        .name(fileName)
                        .build());
            } finally {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            }
        }

        zipFile.close();

        if (!tempZipFile.delete()) {
            throw new RejectedExecutionException("Error while deleting temp zip file");
        }

        return uploadedFiles;
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
        return inputName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

}
