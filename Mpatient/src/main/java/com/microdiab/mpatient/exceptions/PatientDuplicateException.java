package com.microdiab.mpatient.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when an attempt is made to create a duplicate patient record.
 *
 * This exception is annotated with {@link ResponseStatus} to return an HTTP 409 (Conflict)
 * status when triggered, indicating that the request could not be completed due to a conflict
 * with the current state of the resource (e.g., duplicate patient ID or unique field violation).
 *
 * @see org.springframework.web.bind.annotation.ResponseStatus
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PatientDuplicateException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(PatientDuplicateException.class);

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message explaining the duplicate patient conflict.
     */
    public PatientDuplicateException(String message) {
        super(message);
        log.warn("*****  THROW Exception : {} - message : {}", getClass().getName(), getMessage());
    }
}
