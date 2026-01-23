package com.microdiab.mnotes.repository;

import com.microdiab.mnotes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing {@link Note} entities in MongoDB.
 * This interface extends {@link org.springframework.data.mongodb.repository.MongoRepository}
 * to provide CRUD operations and custom query methods for the {@link Note} entity.
 *
 * It is used to interact with the "collection_notes" collection in MongoDB,
 * specifically for retrieving and managing patient notes in the *MicroDiab* application.
 * The repository supports finding notes by the patient's SQL identifier ({@code patId}).
 *
 * @see org.springframework.data.mongodb.repository.MongoRepository
 * @see com.microdiab.mnotes.model.Note
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Finds all notes associated with a specific patient identifier ({@code patId}).
     * This method is used to retrieve all notes for a patient from the MongoDB collection.
     *
     * @param patId The patient identifier from the SQL database.
     * @return A list of {@link Note} entities associated with the given {@code patId}.
     */
    List<Note> findByPatId(Long patId);

}
