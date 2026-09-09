package com.example.iamservice.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
