package com.microdiab.mpatient.repository;

import com.microdiab.mpatient.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest            // Configures a minimal Spring context for testing the JPA layer.
@ActiveProfiles("test") // Optional: to use a specific profile for testing
public class PatientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;        // Allow to manipulate entities in the test database (H2 by default).

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void whenPatientExists_thenReturnTrue() {
        // Arrange
        Patient patient = new Patient();
        patient.setLastname("Dupont");
        patient.setFirstname("Jean");
        patient.setDateofbirth(LocalDate.of(1990, 1, 1));
        patient.setGender("M");
        entityManager.persist(patient);     // Saves a patient in the database for testing.
        entityManager.flush();              // Forces synchronisation with the database.

        // Act
        boolean exists = patientRepository.existsByLastnameAndFirstnameAndDateofbirthAndGender(
                "Dupont", "Jean", LocalDate.of(1990, 1, 1), "M");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void whenPatientDoesNotExist_thenReturnFalse() {
        // Act
        boolean exists = patientRepository.existsByLastnameAndFirstnameAndDateofbirthAndGender(
                "Unknown", "User", LocalDate.of(2000, 1, 1), "F");

        // Assert
        assertThat(exists).isFalse();
    }
}


