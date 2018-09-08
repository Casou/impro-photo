package com.bparent.improPhoto.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

}