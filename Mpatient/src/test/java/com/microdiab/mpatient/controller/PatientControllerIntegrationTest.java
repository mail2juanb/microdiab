package com.microdiab.mpatient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microdiab.mpatient.exceptions.PatientDuplicateException;
import com.microdiab.mpatient.exceptions.PatientNotFoundException;
import com.microdiab.mpatient.model.Patient;
import com.microdiab.mpatient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        patientRepository.deleteAll();
    }


    @Test
    public void testShowPatientList() throws Exception {
        // Arrange
        Patient patient1 = new Patient(null, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        Patient patient2 = new Patient(null, "Martin", "Marie", LocalDate.of(1990, 5, 15), "F", "456 Rue de Lyon", "0987654321");
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Act & Assert
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].lastname").value("Dupont"))
                .andExpect(jsonPath("$[1].lastname").value("Martin"));
    }


    @Test
    public void testShowPatientId_Found() throws Exception {
        // Arrange
        Patient patient = new Patient(null, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        Patient savedPatient = patientRepository.save(patient);

        // Act & Assert
        mockMvc.perform(get("/patient/{id}", savedPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Dupont"));
    }

    @Test
    public void testShowPatientId_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/patient/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
    }


    @Test
    public void testAddPatient_Valid() throws Exception {
        // Arrange
        Patient patient = new Patient(null, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");

        // Act & Assert
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Dupont"));
    }

    @Test
    public void testAddPatient_Invalid() throws Exception {
        // Arrange
        Patient patient = new Patient(null, "", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");

        // Act & Assert
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdatePatient_Valid() throws Exception {
        // Arrange
        Patient patient = new Patient(null, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        Patient savedPatient = patientRepository.save(patient);
        savedPatient.setLastname("NouveauNom");

        // Act & Assert
        mockMvc.perform(put("/patient/{id}", savedPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("NouveauNom"));
    }

    @Test
    public void testUpdatePatient_Invalid() throws Exception {
        // Arrange
        Patient patient = new Patient(1L, "", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");

        // Act & Assert
        mockMvc.perform(put("/patient/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testAddPatient_Duplicate() throws Exception {
        // Arrange
        Patient patient = new Patient(null, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");
        patientRepository.save(patient); // Sauvegarde un premier patient

        // Act & Assert
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient))) // Essaye d'ajouter le même patient
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientDuplicateException));
    }


    @Test
    public void testUpdatePatient_NotFound() throws Exception {
        // Arrange
        Patient patient = new Patient(999L, "Dupont", "Jean", LocalDate.of(1980, 1, 1), "M", "123 Rue de Paris", "0123456789");

        // Act & Assert
        mockMvc.perform(put("/patient/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
    }
}
