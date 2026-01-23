package com.microdiab.mpatient.exception;

import com.microdiab.mpatient.exceptions.PatientDuplicateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientDuplicateExceptionTest {


    @Test
    public void testPatientDuplicateException_Message() {
        // Arrange
        String expectedMessage = "Duplicate patient with name coco";

        // Act & Assert
        PatientDuplicateException exception = assertThrows(
                PatientDuplicateException.class,
                () -> { throw new PatientDuplicateException(expectedMessage); }
        );

        assertEquals(expectedMessage, exception.getMessage());
    }
}
