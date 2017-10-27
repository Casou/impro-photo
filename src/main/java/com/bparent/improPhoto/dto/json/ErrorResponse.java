package com.bparent.improPhoto.dto.json;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse extends ResponseEntity {

    public ErrorResponse(String message) {
        super(new MessageResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
