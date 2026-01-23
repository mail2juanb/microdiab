package com.microdiab.mnotes.controller;

import com.microdiab.mnotes.model.Note;
import com.microdiab.mnotes.service.NoteService;
import com.microdiab.mnotes.tracing.TracingHelper;
import io.micrometer.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST Controller for managing notes related to patients.
 * Provides endpoints to create and retrieve notes for patients.
 */
@RestController
@Tag(name = "mnotes API", description = "API for managing patient notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private TracingHelper tracing;


    /**
     * Creates a new note for a patient.
     *
     * @param note   The note to be created. Must be valid according to the validation rules.
     * @param result BindingResult containing validation errors, if any.
     * @return A ResponseEntity containing the saved note if successful,
     *         or a list of validation errors if validation fails.
     */
    @Operation(
        summary = "Create a new note for a patient",
        description = "Creates and saves a new note for the specified patient."
    )
    @ApiResponse(responseCode = "200", description = "Note successfully created",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Note.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping("/notes")
    @NewSpan("mnotes-create-note")
    public ResponseEntity<?> createNote(@Valid @RequestBody Note note, BindingResult result) {

        tracing.tag("endpoint", "/notes");
        tracing.event("Creating new note");

        if (result.hasErrors()) {
            tracing.error("ValidationError", result.getAllErrors().toString());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Note savedNote = noteService.saveNote(note);
        tracing.tag("note.id", savedNote.getId());
        tracing.event("Note saved successfully");

        return ResponseEntity.ok(savedNote);
    }


    /**
     * Retrieves all notes for a specific patient identified by their ID.
     *
     * @param patId The ID of the patient whose notes are to be retrieved.
     * @return A ResponseEntity containing a list of notes for the specified patient.
     */
    @Operation(
        summary = "Retrieve all notes for a patient",
        description = "Returns all notes associated with the specified patient ID."
    )
    @ApiResponse(responseCode = "200", description = "List of notes for the patient",
                 content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Note.class))))
    @GetMapping("/notes/{patId}")
    @NewSpan("mnotes-get-notes")
    public ResponseEntity<List<Note>> getNotesByPatId(@PathVariable Long patId) {

        tracing.tag("endpoint", "/notes/{patId}");
        tracing.tag("patient.id", patId);
        tracing.event("Fetching notes for patient");

        List<Note> notes = noteService.getNotesByPatId(patId);

        tracing.tag("note.count", notes.size());

        return ResponseEntity.ok(notes);
    }
}
