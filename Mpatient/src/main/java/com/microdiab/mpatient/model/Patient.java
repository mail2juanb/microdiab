package com.microdiab.mpatient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * Entity class representing a patient in the *mPatient* microservice of the *MicroDiab* project.
 * This class maps to the "patient" table in the SQL database and is used to store and manage
 * patient information within the diabetes analysis application.
 *
 * The patient entity includes mandatory fields such as lastname, firstname, date of birth,
 * and gender, as well as optional fields like address and phone number.
 * Validation constraints are applied to ensure data integrity:
 * <ul>
 *   <li>{@link NotBlank} for text fields (lastname, firstname, gender)</li>
 *   <li>{@link NotNull} and {@link Past} for the date of birth</li>
 * </ul>
 *
 * This entity is part of the *mPatient* microservice, which is responsible for managing
 * patient data in the *MicroDiab* application. It is designed to work with a SQL database
 * (preferably MySQL) and includes a unique identifier ({@code id}) that can be used for
 * synchronization with other databases (e.g., MongoDB) if needed.
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.Past
 */
@Entity
@Table(name = "patient")
public class Patient {

    /**
     * Unique identifier for the patient.
     * This field is auto-generated using the {@link GenerationType#IDENTITY} strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Last name of the patient.
     * This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "lastname is mandatory")
    private String lastname;


    /**
     * First name of the patient.
     * This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "firstname is mandatory")
    private String firstname;


    /**
     * Date of birth of the patient.
     * This field is mandatory, must not be null, and must be a date in the past.
     */
    @NotNull(message = "dateofbirth is mandatory")
    @Past(message = "dateofbirth must be in the past")
    private LocalDate dateofbirth;


    /**
     * Gender of the patient.
     * This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "gender is mandatory")
    private String gender;


    /**
     * Address of the patient.
     * This field is optional.
     */
    private String address;


    /**
     * Phone number of the patient.
     * This field is optional.
     */
    private String phone;


    /**
     * Default constructor for JPA.
     */
    public Patient() {
    }


    /**
     * Parameterized constructor for creating a new patient with all fields.
     *
     * @param id          The unique identifier of the patient.
     * @param lastname     The last name of the patient.
     * @param firstname    The first name of the patient.
     * @param dateofbirth  The date of birth of the patient.
     * @param gender       The gender of the patient.
     * @param address      The address of the patient.
     * @param phone        The phone number of the patient.
     */
    public Patient(Long id, String lastname, String firstname, LocalDate dateofbirth, String gender, String address, String phone) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
    }


    /**
     * Returns the unique identifier of the patient.
     *
     * @return the patient's id
     */
    public Long getId() {
        return id;
    }


    /**
     * Sets the unique identifier of the patient.
     *
     * @param id the patient's id
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Returns the last name of the patient.
     *
     * @return the patient's last name
     */
    public String getLastname() {
        return lastname;
    }


    /**
     * Sets the last name of the patient.
     *
     * @param lastname the patient's last name
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    /**
     * Returns the first name of the patient.
     *
     * @return the patient's first name
     */
    public String getFirstname() {
        return firstname;
    }


    /**
     * Sets the first name of the patient.
     *
     * @param firstname the patient's first name
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    /**
     * Returns the date of birth of the patient.
     *
     * @return the patient's date of birth
     */
    public LocalDate getDateofbirth() {
        return dateofbirth;
    }


    /**
     * Sets the date of birth of the patient.
     *
     * @param dateofbirth the patient's date of birth
     */
    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }


    /**
     * Returns the gender of the patient.
     *
     * @return the patient's gender
     */
    public String getGender() {
        return gender;
    }


    /**
     * Sets the gender of the patient.
     *
     * @param gender the patient's gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }


    /**
     * Returns the address of the patient.
     *
     * @return the patient's address
     */
    public String getAddress() {
        return address;
    }


    /**
     * Sets the address of the patient.
     *
     * @param address the patient's address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Returns the phone number of the patient.
     *
     * @return the patient's phone number
     */
    public String getPhone() {
        return phone;
    }


    /**
     * Sets the phone number of the patient.
     *
     * @param phone the patient's phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
