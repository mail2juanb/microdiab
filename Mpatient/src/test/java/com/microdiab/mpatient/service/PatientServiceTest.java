package com.microdiab.mpatient.service;

import com.microdiab.mpatient.exceptions.PatientDuplicateException;
import com.microdiab.mpatient.exceptions.PatientNotFoundException;
import com.microdiab.mpatient.model.Patient;
import com.microdiab.mpatient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setLastname("Dupont");
        patient.setFirstname("Jean");
        patient.setDateofbirth(LocalDate.of(1990, 1, 1));
        patient.setGender("M");
        patient.setAddress("123 Rue de Paris");
        patient.setPhone("0123456789");
    }


    @Test
    void savePatient_shouldSavePatient_whenNoDuplicate() {
        when(patientRepository.existsByLastnameAndFirstnameAndDateofbirthAndGender(
                patient.getLastname(),
                patient.getFirstname(),
                patient.getDateofbirth(),
                patient.getGender()))
                .thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientService.savePatient(patient);

        assertNotNull(savedPatient);
        assertEquals(patient.getLastname(), savedPatient.getLastname());
        verify(patientRepository, times(1)).save(patient);
    }


    @Test
    void savePatient_shouldThrowException_whenDuplicateExists() {
        when(patientRepository.existsByLastnameAndFirstnameAndDateofbirthAndGender(
                patient.getLastname(),
                patient.getFirstname(),
                patient.getDateofbirth(),
                patient.getGender()))
                .thenReturn(true);

        assertThrows(PatientDuplicateException.class, () -> {
            patientService.savePatient(patient);
        });
    }


    @Test
    void updatePatient_shouldUpdatePatient_whenPatientExists() {
        // Existing patient in database
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setLastname("Dupont");
        existingPatient.setFirstname("Jean");
        existingPatient.setDateofbirth(LocalDate.of(1990, 1, 1));
        existingPatient.setGender("M");
        existingPatient.setAddress("123 Rue de Paris");
        existingPatient.setPhone("0123456789");

        // Patient with the new data
        Patient updatedPatient = new Patient();
        updatedPatient.setLastname("Martin");
        updatedPatient.setFirstname("Pierre");
        updatedPatient.setDateofbirth(LocalDate.of(1990, 1, 1));
        updatedPatient.setGender("M");
        updatedPatient.setAddress("456 Rue de Lyon");
        updatedPatient.setPhone("0987654321");

        // Mock of the repository
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Method call
        Patient result = patientService.updatePatient(1L, updatedPatient);

        // Verifications
        assertNotNull(result);
        assertEquals("Martin", result.getLastname());
        assertEquals("Pierre", result.getFirstname());
        assertEquals("456 Rue de Lyon", result.getAddress());
        assertEquals("0987654321", result.getPhone());

        // Verify that save is called with the existing (updated) object
        verify(patientRepository, times(1)).save(existingPatient);
    }



    @Test
    void updatePatient_shouldThrowException_whenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatient(1L, patient);
        });
    }


    @Test
    void updatePatient_shouldThrowPatientNotFoundException_whenUpdatePatientIsNull() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatient(1L, null);
        });
    }

}
