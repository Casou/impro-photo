package com.bparent.improPhoto.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {

    public static String stackTrace(Exception cause) {
        if (cause == null) {
            return "";
        }

        StringWriter sw = new StringWriter(1024);
        final PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static boolean isEmpty(String fileExtension) {
        return fileExtension == null || fileExtension.trim().equals("");
    }
}
