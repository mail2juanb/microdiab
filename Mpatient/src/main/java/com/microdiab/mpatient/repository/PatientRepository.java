package com.microdiab.mpatient.repository;


import com.microdiab.mpatient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Repository interface for managing {@link Patient} entities in the *mPatient* microservice
 * of the *MicroDiab* project.
 *
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom queries
 * for the {@link Patient} entity. It is designed to work with a SQL database (preferably MySQL)
 * and is part of the *mPatient* microservice, which manages patient data for the diabetes analysis application.
 *
 * The repository includes a custom method to check for the existence of a patient
 * based on unique personal details (lastname, firstname, date of birth, and gender).
 * This method is used to prevent duplicate patient records in the system.
 *
 * @see com.microdiab.mpatient.model.Patient
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Checks if a patient with the specified personal details already exists in the database.
     * This method is used to prevent duplicate patient records.
     *
     * @param lastname    The last name of the patient.
     * @param firstname   The first name of the patient.
     * @param dateofbirth The date of birth of the patient.
     * @param gender      The gender of the patient.
     * @return {@code true} if a patient with the specified details exists, {@code false} otherwise.
     */
    boolean existsByLastnameAndFirstnameAndDateofbirthAndGender(
            String lastname,
            String firstname,
            LocalDate dateofbirth,
            String gender
    );
}
