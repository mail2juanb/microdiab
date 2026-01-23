package com.microdiab.mpatient.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when a requested patient record is not found.
 *
 * This exception is annotated with {@link ResponseStatus} to return an HTTP 404 (Not Found)
 * status when triggered, indicating that the requested patient resource does not exist.
 *
 * @see org.springframework.web.bind.annotation.ResponseStatus
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(PatientNotFoundException.class);

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message explaining why the patient was not found.
     */
    public PatientNotFoundException(String message) {
        super(message);
        log.warn("*****  THROW Exception : {} - message : {}", getClass().getName(), getMessage());
    }
}
