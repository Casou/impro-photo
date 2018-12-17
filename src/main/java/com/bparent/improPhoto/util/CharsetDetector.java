package com.bparent.improPhoto.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class CharsetDetector {

    private static final String[] CHARSETS_TO_BE_TESTED = {"Cp437", "UTF-8", "windows-1253", "ISO-8859-7"};

    public static Charset detectCharset(File f) {

        Charset charset = null;

        for (String charsetName : CHARSETS_TO_BE_TESTED) {
            charset = detectCharset(f, Charset.forName(charsetName));
            if (charset != null) {
                break;
            }
        }

        return charset;
    }

    private static Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }

}
