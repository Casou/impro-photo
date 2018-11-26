package com.bparent.improPhoto.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void stackTrace_shouldReturnStringFromSeveralExceptions() {
        Exception parentException = new IllegalArgumentException("Parent Exception");
        Exception middleException = new IOException("Middle Exception", parentException);
        Exception childException = new Exception("Child Exception", middleException);
        String stackTrace = StringUtils.stackTrace(childException);
        assertEquals("java.lang.Exception: Child Exception", stackTrace.split("\n")[0].trim());
        assertTrue(stackTrace.contains("Caused by: java.io.IOException: Middle Exception"));
        assertTrue(stackTrace.contains("Caused by: java.lang.IllegalArgumentException: Parent Exception"));
    }

}