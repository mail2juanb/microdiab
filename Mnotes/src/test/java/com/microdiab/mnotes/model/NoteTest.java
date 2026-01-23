package com.microdiab.mnotes.model;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class NoteTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidNote() {
        Note note = new Note("123", 1L, "Jean Dupont", "Note valide");
        var violations = validator.validate(note);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidPatId() {
        Note note = new Note("123", -1L, "Jean Dupont", "Note valide");
        var violations = validator.validate(note);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidPatient() {
        Note note = new Note("123", 1L, "", "Note valide");
        var violations = validator.validate(note);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidNote() {
        Note note = new Note("123", 1L, "Jean Dupont", "");
        var violations = validator.validate(note);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testGetIdAndSetId() {
        Note note = new Note();
        // Test with a valid ID
        note.setId("test-id-123");
        assertEquals("test-id-123", note.getId());

        // Test with a null ID
        note.setId(null);
        assertNull(note.getId());

        // Test with an empty ID
        note.setId("");
        assertEquals("", note.getId());
    }

    @Test
    void testToString_WithAllFieldsSet() {
        Note note = new Note("123", 1L, "Jean Dupont", "Note de test");
        String expected = "Note{patId=1, patient='Jean Dupont', note='Note de test'}";
        assertEquals(expected, note.toString());
    }

    @Test
    void testToString_WithNullFields() {
        Note note = new Note();
        note.setPatId(null);
        note.setPatient(null);
        note.setNote(null);
        String result = note.toString();
        assertTrue(result.contains("patId=null"));
        assertTrue(result.contains("patient='null'"));
        assertTrue(result.contains("note='null'"));
    }

    @Test
    void testToString_WithEmptyFields() {
        Note note = new Note();
        note.setPatId(0L);
        note.setPatient("");
        note.setNote("");
        String result = note.toString();
        assertTrue(result.contains("patId=0"));
        assertTrue(result.contains("patient=''"));
        assertTrue(result.contains("note=''"));
    }
}
