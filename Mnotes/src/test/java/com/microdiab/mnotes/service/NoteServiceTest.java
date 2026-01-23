package com.microdiab.mnotes.service;

import com.microdiab.mnotes.model.Note;
import com.microdiab.mnotes.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        note1 = new Note("1", 1001L, "Patient A", "Première note pour le patient A.");
        note2 = new Note("2", 1001L, "Patient A", "Deuxième note pour le patient A.");
    }


    @Test
    void saveNote_shouldReturnSavedNote() {
        // Arrange
        when(noteRepository.save(note1)).thenReturn(note1);

        // Act
        Note savedNote = noteService.saveNote(note1);

        // Assert
        assertNotNull(savedNote);
        assertEquals(note1, savedNote);
        verify(noteRepository, times(1)).save(note1);
    }


    @Test
    void getNotesByPatId_shouldReturnListOfNotes() {
        // Arrange
        Long patId = 1001L;
        List<Note> expectedNotes = Arrays.asList(note1, note2);
        when(noteRepository.findByPatId(patId)).thenReturn(expectedNotes);

        // Act
        List<Note> actualNotes = noteService.getNotesByPatId(patId);

        // Assert
        assertNotNull(actualNotes);
        assertEquals(2, actualNotes.size());
        assertEquals(expectedNotes, actualNotes);
        verify(noteRepository, times(1)).findByPatId(patId);
    }


    @Test
    void getNotesByPatId_shouldReturnEmptyListWhenNoNotesFound() {
        // Arrange
        Long patId = 9999L;
        when(noteRepository.findByPatId(patId)).thenReturn(Arrays.asList());

        // Act
        List<Note> actualNotes = noteService.getNotesByPatId(patId);

        // Assert
        assertNotNull(actualNotes);
        assertTrue(actualNotes.isEmpty());
        verify(noteRepository, times(1)).findByPatId(patId);
    }

    @Test
    void saveNote_shouldThrowExceptionWhenRepositoryFails() {
        // Arrange
        when(noteRepository.save(note1)).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> noteService.saveNote(note1));
    }

    @Test
    void getNotesByPatId_shouldThrowExceptionWhenPatIdIsNull() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.getNotesByPatId(null)
        );
        assertEquals("patId cannot be null", exception.getMessage());
        verify(noteRepository, never()).findByPatId(any());
    }

    @Test
    void getNotesByPatId_shouldThrowExceptionWhenRepositoryFails() {
        // Arrange
        Long patId = 1001L;
        when(noteRepository.findByPatId(patId)).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> noteService.getNotesByPatId(patId));
    }
}
