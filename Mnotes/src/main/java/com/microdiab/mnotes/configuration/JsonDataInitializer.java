package com.microdiab.mnotes.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microdiab.mnotes.model.Note;
import com.microdiab.mnotes.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


/**
 * The {@code JsonDataInitializer} class is a Spring Boot component responsible for initializing
 * the application with sample note data from a JSON file. It implements the {@link CommandLineRunner}
 * interface to execute data initialization logic at application startup.
 *
 * This class reads a JSON file containing note data, filters out any existing notes to avoid duplicates,
 * and saves new notes to the database using the provided {@link NoteRepository}.
 *
 * It is designed to be used in the context of the <em>MicroDiab</em> project, specifically for the
 * <em>mNotes</em> microservice, to ensure that the MongoDB database is populated with initial data
 * if it is empty or if new notes are added to the JSON file.
 */
@Profile("!test")
@Component
public class JsonDataInitializer implements CommandLineRunner {

    /** Repository for accessing and managing {@link Note} entities in the database. */
    private final NoteRepository noteRepository;

    /** Jackson {@link ObjectMapper} for JSON serialization and deserialization. */
    private final ObjectMapper objectMapper;


    /**
     * Constructs a new {@code JsonDataInitializer} with the specified dependencies.
     *
     * @param noteRepository The repository used to interact with the database.
     * @param objectMapper   The Jackson {@link ObjectMapper} used to parse JSON data.
     */
    public JsonDataInitializer(NoteRepository noteRepository, ObjectMapper objectMapper) {
        this.noteRepository = noteRepository;
        this.objectMapper = objectMapper;
    }


    /**
     * Initializes the database with note data from a JSON file.
     * This method is automatically executed at application startup due to the
     * {@link CommandLineRunner} interface implementation.
     *
     * It performs the following steps:
     * <ol>
     *   <li>Loads the JSON file from the classpath resource located at {@code data/notes.json}.</li>
     *   <li>Converts the JSON data into an array of {@link Note} objects.</li>
     *   <li>Retrieves all existing notes from the database.</li>
     *   <li>Filters out notes that already exist in the database to avoid duplicates.</li>
     *   <li>Saves the new notes to the database and logs the result.</li>
     * </ol>
     *
     * @param args Command-line arguments (not used in this implementation).
     * @throws Exception If an error occurs during JSON parsing or database operations.
     */
    @Override
    public void run(String... args) throws Exception {

        // Load the JSON file from resources
        InputStream inputStream = new ClassPathResource("data/notes.json").getInputStream();

        // Converts JSON to Note objects
        Note[] notes = objectMapper.readValue(inputStream, Note[].class);

        // Retrieve all existing notes
        List<Note> existingNotes = noteRepository.findAll();

        // Filters the notes to be added (those that do not already exist according to patId, patient, and note)
        List<Note> newNotes = Arrays.stream(notes)
                .filter(note ->
                        existingNotes.stream()
                                .noneMatch(existingNote ->
                                        existingNote.getPatId().equals(note.getPatId()) &&
                                                existingNote.getPatient().equals(note.getPatient()) &&
                                                existingNote.getNote().equals(note.getNote())
                                )
                )
                .toList();

        // Saves only new notes
        if (!newNotes.isEmpty()) {
            noteRepository.saveAll(newNotes);
            System.out.println("New JSON data inserted into MongoDB : " + newNotes.size() + " added notes.");
        } else {
            System.out.println("No new notes to add.");
        }
    }
}
