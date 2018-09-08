package com.bparent.improPhoto.exception;

public class ImproServiceException extends Exception {

    public ImproServiceException(String message, Exception e) {
        super(message, e);
    }

    public ImproServiceException(String s) {
    }
}
