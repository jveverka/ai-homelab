package com.example.iamservice.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
