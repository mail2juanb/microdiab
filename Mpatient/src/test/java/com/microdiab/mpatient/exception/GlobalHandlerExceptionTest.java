package com.microdiab.mpatient.exception;

import com.microdiab.mpatient.exceptions.GlobalHandlerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalHandlerExceptionTest {

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @InjectMocks
    private GlobalHandlerException globalHandlerException;

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithErrors() {
        // Arrange
        String fieldName = "email";
        String errorMessage = "must be a valid email";
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        when(fieldError.getField()).thenReturn(fieldName);
        when(fieldError.getDefaultMessage()).thenReturn(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = globalHandlerException.handleValidationExceptions(exception);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(errorMessage, response.getBody().get(fieldName));
    }
}
