package com.microdiab.mpatient.controller;

import com.microdiab.mpatient.exceptions.PatientNotFoundException;
import com.microdiab.mpatient.model.Patient;
import com.microdiab.mpatient.repository.PatientRepository;
import com.microdiab.mpatient.service.PatientService;
import com.microdiab.mpatient.tracing.TracingHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private TracingHelper tracing;

    @InjectMocks
    private PatientController patientController;


    @Test
    public void testShowPatientList() {
        // Arrange
        Patient patient1 = new Patient(1L, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        Patient patient2 = new Patient(2L, "Martin", "Marie", LocalDate.of(1990, 5, 15), "F", "456 Rue de Lyon", "0987654321");
        List<Patient> patients = Arrays.asList(patient1, patient2);

        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        ResponseEntity<List<Patient>> response = patientController.showPatientList();

        // Assert
        assertEquals(2, response.getBody().size());
        assertEquals("Dupont", response.getBody().get(0).getLastname());
    }


    @Test
    public void testShowPatientId_Found() {
        // Arrange
        Patient patient = new Patient(1L, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Act
        ResponseEntity<Patient> response = patientController.showPatientId(1L);

        // Assert
        assertEquals("Dupont", response.getBody().getLastname());
    }

    @Test
    public void testShowPatientId_NotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientController.showPatientId(1L);
        });
    }


    @Test
    public void testAddPatient_Valid() {
        // Arrange
        Patient patient = new Patient(null, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        Patient savedPatient = new Patient(1L, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        BindingResult result = mock(BindingResult.class);

        when(result.hasErrors()).thenReturn(false);
        when(patientService.savePatient(patient)).thenReturn(savedPatient);

        // Act
        ResponseEntity<?> response = patientController.addPatient(patient, result);

        // Assert
        assertEquals(1L, ((Patient) response.getBody()).getId());
    }

    @Test
    public void testAddPatient_Invalid() {
        // Arrange
        Patient patient = new Patient(null, "", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        BindingResult result = mock(BindingResult.class);

        when(result.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = patientController.addPatient(patient, result);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    public void testUpdatePatient_Valid() {
        // Arrange
        Patient patient = new Patient(1L, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        Patient updatedPatient = new Patient(1L, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        BindingResult result = mock(BindingResult.class);

        when(result.hasErrors()).thenReturn(false);
        when(patientService.updatePatient(1L, patient)).thenReturn(updatedPatient);

        // Act
        ResponseEntity<?> response = patientController.updatePatient(1L, patient, result);

        // Assert
        assertEquals(1L, ((Patient) response.getBody()).getId());
    }

    @Test
    public void testUpdatePatient_Invalid() {
        // Arrange
        Patient patient = new Patient(1L, "", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        BindingResult result = mock(BindingResult.class);

        when(result.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = patientController.updatePatient(1L, patient, result);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }
}
