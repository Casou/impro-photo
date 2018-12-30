package com.bparent.improPhoto.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void shouldExtractFileExtension() {
        assertEquals("jpg", FileUtils.getFileExtension("path/to/file.jpg"));
        assertEquals("jpg", FileUtils.getFileExtension("path\\to\\file.jpg"));
        assertEquals("jpg", FileUtils.getFileExtension("file.jpg"));
        assertEquals("jpg", FileUtils.getFileExtension("file.wrong.jpg"));
    }

    @Test
    public void shouldFormatBackslash() {
        assertEquals("my/path/formatted", FileUtils.formatPathWithCharacter("my\\path\\formatted", "/"));
    }

    @Test
    public void shouldFormatWithSystemSeparator() {
        assertEquals(FileUtils.formatPathWithCharacter("my\\path\\formatted", File.separator),
                FileUtils.formatPathToSystemDefault("my\\path\\formatted"));
    }

    @Test
    public void getFrontFilePath_shouldReturnStringWithHandle() {
        assertEquals("/handler/file.txt", FileUtils.getFrontFilePath(new File("file.txt")));
    }

    @Test
    public void getFileName_shouldReturnString() {
        assertEquals("file.txt", FileUtils.getFileName("/some/path/to/file.txt"));
    }

    @Test
    public void capitalizeCategoryFolderName_shouldCapitalizeString() {
        assertEquals("Décor", FileUtils.capitalizeCategoryFolderName("01 - décor"));
        assertEquals("Décor De NUIT", FileUtils.capitalizeCategoryFolderName("01 - décor De NUIT   "));
        assertEquals("Écureuil", FileUtils.capitalizeCategoryFolderName("écureuil"));
        assertEquals("ÉCUREUIL", FileUtils.capitalizeCategoryFolderName("ÉCUREUIL"));
    }

    @Test
    public void sanitizeFilename_shouldSanitizeString() {
        assertEquals("01_-_d_cor", FileUtils.sanitizeFilename("01 - décor"));
        assertEquals("01_-_d_cor/", FileUtils.sanitizeFilename("01 - décor/"));
    }

    @Test
    public void getFolderName_shouldReturnString() {
        assertEquals("", FileUtils.getFolderName("/file.txt"));
        assertEquals("folder/", FileUtils.getFolderName("folder/file.txt"));
        assertEquals("/folder/", FileUtils.getFolderName("/folder/file.txt"));
        assertEquals("/folder/sub-folder/", FileUtils.getFolderName("/folder/sub-folder/file.txt"));

        assertEquals("", FileUtils.getFolderName("/"));
        assertEquals("/folder/", FileUtils.getFolderName("/folder/sub-folder/"));
    }

}