package com.bparent.improPhoto.dto.json;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponse extends ResponseEntity {

    public SuccessResponse(String message) {
        super(new MessageResponse(message), HttpStatus.OK);
    }

}
