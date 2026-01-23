package com.microdiab.mnotes.service;

import com.microdiab.mnotes.model.Note;
import com.microdiab.mnotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for managing {@link Note} entities in the *MicroDiab* application.
 * This class provides business logic and data access operations for patient notes,
 * interacting with the {@link NoteRepository} to perform CRUD operations and custom queries.
 *
 * It is responsible for saving and retrieving patient notes, ensuring data integrity
 * and validation before delegating to the repository layer.
 *
 * @see com.microdiab.mnotes.model.Note
 * @see com.microdiab.mnotes.repository.NoteRepository
 */
@Service
public class NoteService {

    /**
     * Repository for accessing and managing {@link Note} entities in MongoDB.
     */
    @Autowired
    private NoteRepository noteRepository;


    /**
     * Saves a patient note to the MongoDB database.
     * Validates that the provided note is not null before saving.
     *
     * @param note The note to be saved. Must not be null.
     * @return The saved note, including the generated unique identifier.
     * @throws IllegalArgumentException If the provided note is null.
     */
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }


    /**
     * Retrieves all notes associated with a specific patient identifier ({@code patId}).
     * Validates that the provided patient identifier is not null before querying.
     *
     * @param patId The patient identifier from the SQL database. Must not be null.
     * @return A list of {@link Note} entities associated with the given {@code patId}.
     * @throws IllegalArgumentException If the provided patient identifier is null.
     */
    public List<Note> getNotesByPatId(Long patId) {
        if (patId == null) {
            throw new IllegalArgumentException("patId cannot be null");
        }
        return noteRepository.findByPatId(patId);
    }
}
