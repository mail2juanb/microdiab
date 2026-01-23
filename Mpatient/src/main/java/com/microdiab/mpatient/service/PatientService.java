package com.microdiab.mpatient.service;

import com.microdiab.mpatient.exceptions.PatientDuplicateException;
import com.microdiab.mpatient.exceptions.PatientNotFoundException;
import com.microdiab.mpatient.model.Patient;
import com.microdiab.mpatient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for managing {@link Patient} entities.
 *
 * This service provides operations to create and update patients while ensuring
 * business rules such as duplicate prevention and existence verification.
 *
 * @see PatientRepository
 * @see PatientDuplicateException
 * @see PatientNotFoundException
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    /**
     * Default constructor for {@link PatientService}.
     *
     * This constructor is used by Spring to instantiate the service as a singleton bean.
     * All dependencies are injected automatically.
     */
    public PatientService() {
        // Spring will inject dependencies
    }


    /**
     * Saves a new patient in the database.
     *
     * Before saving, this method checks whether a patient already exists with the same
     * last name, first name, date of birth, and gender. If such a patient is found,
     * a {@link PatientDuplicateException} is thrown.
     *
     * @param patient the patient entity to be saved
     * @return the saved {@link Patient} instance
     * @throws PatientDuplicateException if a patient with identical identifying information already exists
     */
    public Patient savePatient(Patient patient) {
        // Check whether a patient with the same criteria already exists.
        if (patientRepository.existsByLastnameAndFirstnameAndDateofbirthAndGender(
                patient.getLastname(),
                patient.getFirstname(),
                patient.getDateofbirth(),
                patient.getGender())) {
            throw new PatientDuplicateException ("A patient already exists with the same last name, first name, date of birth, and gender.");
        }
        // Save the patient
        return patientRepository.save(patient);
    }


    /**
     * Updates an existing patient.
     *
     * The method first verifies that the patient with the given identifier exists.
     * If the patient cannot be found, a {@link PatientNotFoundException} is thrown.
     * Otherwise, all modifiable fields are updated from the provided patient object.
     *
     * @param id            the identifier of the patient to update
     * @param updatePatient the patient object containing updated information
     * @return the updated and persisted {@link Patient} instance
     * @throws PatientNotFoundException if no patient exists with the specified id
     */
    public Patient updatePatient(Long id, Patient updatePatient) {
        // Checks whether the patient exists
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Le patient avec l'ID " + id + " n'existe pas."));

        // Update patient information
        existingPatient.setLastname(updatePatient.getLastname());
        existingPatient.setFirstname(updatePatient.getFirstname());
        existingPatient.setDateofbirth(updatePatient.getDateofbirth());
        existingPatient.setGender(updatePatient.getGender());
        existingPatient.setAddress(updatePatient.getAddress());
        existingPatient.setPhone(updatePatient.getPhone());

        // Save changes
        return patientRepository.save(existingPatient);
    }
}

