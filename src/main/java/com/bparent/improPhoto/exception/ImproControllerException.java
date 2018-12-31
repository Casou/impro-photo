package com.bparent.improPhoto.exception;

public class ImproControllerException extends Exception {

    public ImproControllerException(String message) {
        super(message);
    }
    public ImproControllerException(String message, Exception e) {
        super(message, e);
    }

}
