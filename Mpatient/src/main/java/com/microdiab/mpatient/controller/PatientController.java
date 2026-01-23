package com.microdiab.mpatient.controller;


import com.microdiab.mpatient.exceptions.PatientNotFoundException;
import com.microdiab.mpatient.repository.PatientRepository;
import com.microdiab.mpatient.model.Patient;
import com.microdiab.mpatient.service.PatientService;
import com.microdiab.mpatient.tracing.TracingHelper;
import io.micrometer.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * REST Controller for managing patient-related operations.
 * This class provides endpoints to list, retrieve, add, and update patient records.
 * It integrates with {@link PatientRepository} and {@link PatientService} for data access and business logic.
 *
 * All endpoints are documented using OpenAPI/Swagger annotations for API clarity and testing.
 *
 * @see com.microdiab.mpatient.repository.PatientRepository
 * @see com.microdiab.mpatient.service.PatientService
 * @see com.microdiab.mpatient.model.Patient
 */
@RestController
@Tag(name = "mpatient API", description = "API for patient management")
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    /** Repository for accessing patient data. */
    private final PatientRepository patientRepository;

    /** Service for handling patient business logic. */
    private final PatientService patientService;

    /** Service for tracing business logic. */
    private final TracingHelper tracing;


    /**
     * Constructs a new {@code PatientController} with the specified repository and service.
     *
     * This constructor is used by Spring to inject dependencies via constructor injection.
     *
     * @param patientRepository the repository for patient data access
     * @param patientService    the service handling patient business logic
     */
    @Autowired
    public PatientController(PatientRepository patientRepository, PatientService patientService, TracingHelper tracing) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        this.tracing = tracing;
    }


    /**
     * Retrieves the complete list of registered patients.
     *
     * @return A {@link ResponseEntity} containing the list of patients.
     */
    @Operation(summary = "List all patients", description = "Returns the complete list of registered patients.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of patients",
                     content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = Patient.class))))
    })
    @GetMapping("/patients")
    @NewSpan("mpatient-list-patients")
    public ResponseEntity<List<Patient>> showPatientList() {

        tracing.tag("endpoint", "/patients");
        tracing.event("Fetching all patients");

        List<Patient> patients = patientRepository.findAll();

        tracing.tag("patient.count", patients.size());

        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a specific patient by their ID.
     *
     * @param id The ID of the patient to retrieve.
     * @return A {@link ResponseEntity} containing the patient details.
     * @throws PatientNotFoundException If the patient does not exist.
     */
    @Operation(summary = "Retrieve a patient by ID", description = "Returns details for a specific patient.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient found",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Patient.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/{id}")
    @NewSpan("mpatient-get-patient")
    public ResponseEntity<Patient> showPatientId(@PathVariable Long id) {

        tracing.tag("endpoint", "/patient/{id}");
        tracing.tag("patient.id", id);
        tracing.event("Fetching patient");

        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isEmpty()) {
            tracing.error("PatientNotFound","Patient not found with ID " + id);
            throw new PatientNotFoundException("The patient corresponding to the ID " + id + " does not exist.");
        }

        return ResponseEntity.ok(patient.get());
    }


    /**
     * Registers a new patient in the database.
     *
     * @param patient The patient object to add.
     * @param result The binding result for validation.
     * @return A {@link ResponseEntity} containing the saved patient or validation errors.
     */
    @Operation(summary = "Add a new patient", description = "Registers a new patient in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient successfully added",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Patient.class))),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/patient")
    @NewSpan("mpatient-add-patient")
    public ResponseEntity<?> addPatient(@Valid @RequestBody Patient patient, BindingResult result) {

        tracing.tag("endpoint", "/patient");
        tracing.event("Adding new patient");

        if (result.hasErrors()) {
            tracing.error("ValidationError", result.getAllErrors().toString());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Patient savedPatient = patientService.savePatient(patient);

        tracing.tag("patient.id", savedPatient.getId());
        tracing.event("Patient saved successfully");

        log.debug("Patient recorded with l'ID : {}", savedPatient.getId());

        return ResponseEntity.ok(savedPatient);
    }


    /**
     * Updates the information for an existing patient.
     *
     * @param id The ID of the patient to update.
     * @param updatePatient The updated patient object.
     * @param result The binding result for validation.
     * @return A {@link ResponseEntity} containing the updated patient or validation errors.
     */
    @Operation(summary = "Updates a patient", description = "Updates the information for an existing patient.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient successfully updated",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Patient.class))),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/patient/{id}")
    @NewSpan("mpatient-update-patient")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient updatePatient, BindingResult result) {

        tracing.tag("endpoint", "/patient/{id}");
        tracing.tag("patient.id", id);
        tracing.event("Updating patient");

        if (result.hasErrors()) {
            tracing.error("ValidationError", result.getAllErrors().toString());
            log.debug("Patient not saved, validation error : {}", result.getAllErrors());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Patient updatedPatient = patientService.updatePatient(id, updatePatient);

        tracing.event("Patient updated successfully");

        log.debug("Patient successfully updated : {}", updatedPatient.getLastname());
        return ResponseEntity.ok(updatedPatient);
    }

}
