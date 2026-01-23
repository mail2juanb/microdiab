package com.microdiab.mnotes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microdiab.mnotes.model.Note;
import com.microdiab.mnotes.service.NoteService;
import com.microdiab.mnotes.tracing.TracingHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoteService noteService;

    @MockitoBean
    private TracingHelper tracing;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        note1 = new Note("id124", 15L, "Patient 1", "Note du patient 1");
        note2 = new Note("id421", 16L, "Patient 2", "Note du patient 2");
    }


    // Verify that the controller returns the saved note with a 200 OK status if the note is valid.
    @Test
    void createNote_shouldReturnSavedNote_whenNoteIsValid() throws Exception {
        when(noteService.saveNote(any(Note.class))).thenReturn(note1);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id124"))
                .andExpect(jsonPath("$.note").value("Note du patient 1"));
    }

    // Verifies that the controller returns a 400 Bad Request status if the note is invalid (validation errors).
    @Test
    void createNote_shouldReturnBadRequest_whenNoteIsInvalid() throws Exception {
        Note invalidNote = new Note("id124", 15L, "Patient 1", ""); // Note vide

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNote)))
                .andExpect(status().isBadRequest());
    }

    // Verifies that the controller returns a list of notes for a given patId, with a status of 200 OK.
    @Test
    void getNotesByPatId_shouldReturnListOfNotes() throws Exception {
        List<Note> notes = Arrays.asList(note1, note2);
        when(noteService.getNotesByPatId(anyLong())).thenReturn(notes);

        mockMvc.perform(get("/notes/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("id124"))
                .andExpect(jsonPath("$[0].note").value("Note du patient 1"))
                .andExpect(jsonPath("$[1].id").value("id421"))
                .andExpect(jsonPath("$[1].note").value("Note du patient 2"));
    }
}
