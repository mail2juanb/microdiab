package com.microdiab.mnotes.repository;

import com.microdiab.mnotes.model.Note;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;


    @AfterEach
    public void cleanUp() {
        noteRepository.deleteAll();
    }


    @Test
    public void testFindByPatId_ShouldReturnNotesForGivenPatId() {
        // Arrange
        Long patId = 90L;
        Note note1 = new Note(null, patId, "Patient A", "Note 1");
        Note note2 = new Note(null, patId, "Patient A", "Note 2");
        Note note3 = new Note(null, 92L, "Patient B", "Note 3");

        noteRepository.saveAll(List.of(note1, note2, note3));

        // Act
        List<Note> foundNotes = noteRepository.findByPatId(patId);

        // Assert
        assertThat(foundNotes).hasSize(2);
        assertThat(foundNotes)
                .extracting(Note::getPatId)
                .containsOnly(patId);
    }


    @Test
    public void testFindByPatId_ShouldReturnEmptyListForNonExistentPatId() {
        // Arrange
        Long nonExistentPatId = 999L;

        // Act
        List<Note> foundNotes = noteRepository.findByPatId(nonExistentPatId);

        // Assert
        assertThat(foundNotes).isEmpty();
    }
}
