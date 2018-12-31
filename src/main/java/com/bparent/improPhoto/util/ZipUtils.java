package com.bparent.improPhoto.util;

import com.bparent.improPhoto.dto.UploadedFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ZipUtils {

    public static List<UploadedFileDto> copyAnyUploadedFile(MultipartFile multipart, Predicate<String> isAcceptedFile,
                                                               List<String> fileExtensionAccepted, String destinationFolder,
                                                               boolean flatPath) {
        String originalFilename = multipart.getOriginalFilename();
        String fileExtension = FileUtils.getFileExtension(originalFilename);

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
            return Arrays.asList(copySingleUploadedFile(multipart, originalFilename, destinationFolder));
        }
    }

    public static UploadedFileDto copySingleUploadedFile(MultipartFile multipart, String originalFilename, String destinationFolder) {
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
            String fileExtension = FileUtils.getFileExtension(filePath);
            uniqueFilePath = FileUtils.getFileNameWithoutExtension(filePath) + "_" + cpt
                    + (!StringUtils.isEmpty(fileExtension) ? "." + fileExtension : "");
            f = new File(uniqueFilePath);
            cpt++;
        }
        return uniqueFilePath;
    }

    private static List<UploadedFileDto> unzipUploadedFile(MultipartFile multipart, String fileZip, List<String> fileExtensionsAccepted,
                                          String destinationFolder, boolean flatPath) throws IOException {
        log.debug("Unzip file...");
        final List<UploadedFileDto> zipFiles = new ArrayList<>();

        final UploadedFileDto uploadedFileDto = copySingleUploadedFile(multipart, fileZip, IConstants.IPath.MEDIAS_TEMP);
        final File tempZipFile = new File(IConstants.IPath.MEDIAS_TEMP + uploadedFileDto.getFileName());
        final ZipFile zipFile = new ZipFile(tempZipFile, CharsetDetector.detectCharset(tempZipFile));

        zipFile.stream().forEach(zipEntry -> extractZipEntry(zipEntry, zipFiles, fileExtensionsAccepted, flatPath));

        Stream<UploadedFileDto> uploadedFileDtoStream = zipFiles.stream();
        if (flatPath) {
            uploadedFileDtoStream = uploadedFileDtoStream.filter(UploadedFileDto::getIsFile);
        }
        final List<UploadedFileDto> uploadedFiles = uploadedFileDtoStream
                .map(dto -> ZipUtils.extractZipFile(zipFile, dto, destinationFolder))
                .collect(Collectors.toList());

        zipFile.close();

        if (!tempZipFile.delete()) {
            throw new RejectedExecutionException("Error while deleting temp zip file");
        }

        return uploadedFiles;
    }

    public static UploadedFileDto extractZipFile(ZipFile zipFile, UploadedFileDto uploadedFileDto, String destinationFolder) {
        int b;
        byte[] buffer = new byte[1024];

        //create directories for sub directories in zip
        if (!uploadedFileDto.getIsFile()) {
            final String filePath = destinationFolder + uploadedFileDto.getParentPath() + uploadedFileDto.getFileName();
            final File destinationFile = new File(getUniqueFilePath(filePath));
            uploadedFileDto.setFileName(destinationFile.getName());
            uploadedFileDto.setUploadSuccess(destinationFile.mkdir());
            return uploadedFileDto;
        }

        // Copy file
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(zipFile.getInputStream(zipFile.getEntry(uploadedFileDto.getOriginalPath())));

            final String filePath = destinationFolder + uploadedFileDto.getParentPath() + uploadedFileDto.getFileName();
            final File destinationFile = new File(getUniqueFilePath(filePath));
            uploadedFileDto.setFileName(destinationFile.getName());

            final FileOutputStream fos = new FileOutputStream(destinationFile);
            bos = new BufferedOutputStream(fos, 1024);

            while ((b = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, b);
            }

            uploadedFileDto.setUploadSuccess(true);
        } catch (IOException ioe) {
            uploadedFileDto.setUploadSuccess(false);
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException ioe) {
                    uploadedFileDto.setUploadSuccess(false);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    uploadedFileDto.setUploadSuccess(false);
                }
            }
        }
        return uploadedFileDto;
    }


    public static List<UploadedFileDto> listZipFiles(ZipFile zipFile, List<String> fileExtensionsAccepted, boolean flatPath) {
        log.debug("Unzip file...");
        final List<UploadedFileDto> uploadedFiles = new ArrayList<>();

        zipFile.stream().forEach(zipEntry -> extractZipEntry(zipEntry, uploadedFiles, fileExtensionsAccepted, flatPath));

        return uploadedFiles;
    }

    private static void extractZipEntry(ZipEntry entry, List<UploadedFileDto> uploadedFiles,
                                        List<String> fileExtensionsAccepted, boolean flatPath) {
        String entryPath = entry.getName();
        final String fileName = FileUtils.getFileName(entryPath);
        if (flatPath) {
            entryPath = fileName;
        }
        String dtoName = fileName;

        final String folderPath = FileUtils.getFolderName(entryPath);
        Optional<UploadedFileDto> optFolderDto = uploadedFiles.stream()
                .flatMap(UploadedFileDto::streamChildren)
                .filter(dto -> dto.getOriginalPath().equals(folderPath))
                .findFirst();

        boolean isFile = true;

        if (entry.isDirectory()) {
            log.debug("\t\tDirectory : " + entryPath);
            isFile = false;
        } else {
            if (fileExtensionsAccepted != null && fileExtensionsAccepted.size() > 0 &&
                    !fileExtensionsAccepted.contains(FileUtils.getFileExtension(entryPath))) {
                log.info("\t\tFile : " + entryPath + " (rejected)");
                return;
            }

            log.debug("\t\tFile : " + entryPath);
            dtoName = FileUtils.getFileNameWithoutExtension(dtoName);
        }

        UploadedFileDto uploadedFileDto = UploadedFileDto.builder()
                .originalPath(entry.getName())
                .fileName(FileUtils.sanitizeFilename(fileName))
                .name(dtoName)
                .isFile(isFile)
                .parent(optFolderDto.orElse(null))
                .build();
        if (optFolderDto.isPresent()) {
            optFolderDto.get().getChildren().add(uploadedFileDto);
        } else {
            uploadedFiles.add(uploadedFileDto);
        }
    }

}
