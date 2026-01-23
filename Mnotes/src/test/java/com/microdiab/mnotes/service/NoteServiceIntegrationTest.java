package com.microdiab.mnotes.service;

import com.microdiab.mnotes.model.Note;
import com.microdiab.mnotes.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class NoteServiceIntegrationTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void saveNote_shouldSaveNoteSuccessfully() {
        // Arrange
        Note note = new Note();
        note.setId("id345");
        note.setPatId(100L);
        note.setNote("Test note content");

        when(noteRepository.save(note)).thenReturn(note);

        // Act
        Note savedNote = noteService.saveNote(note);

        // Assert
        assertNotNull(savedNote);
        assertEquals("id345", savedNote.getId());
        assertEquals(100L, savedNote.getPatId());
        assertEquals("Test note content", savedNote.getNote());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void getNotesByPatId_shouldReturnNotesForValidPatId() {
        // Arrange
        Long patId = 100L;
        Note note1 = new Note();
        note1.setId("id456");
        note1.setPatId(patId);
        note1.setNote("Note 1");

        Note note2 = new Note();
        note2.setId("id678");
        note2.setPatId(patId);
        note2.setNote("Note 2");

        List<Note> expectedNotes = Arrays.asList(note1, note2);
        when(noteRepository.findByPatId(patId)).thenReturn(expectedNotes);

        // Act
        List<Note> actualNotes = noteService.getNotesByPatId(patId);

        // Assert
        assertNotNull(actualNotes);
        assertEquals(2, actualNotes.size());
        assertEquals("Note 1", actualNotes.get(0).getNote());
        assertEquals("Note 2", actualNotes.get(1).getNote());
        verify(noteRepository, times(1)).findByPatId(patId);
    }

    @Test
    void getNotesByPatId_shouldThrowExceptionWhenPatIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            noteService.getNotesByPatId(null);
        });
    }
}

