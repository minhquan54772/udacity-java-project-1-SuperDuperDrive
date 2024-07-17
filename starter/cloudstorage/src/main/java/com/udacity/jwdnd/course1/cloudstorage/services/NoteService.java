package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotesByUserId(int userId) {
        return this.noteMapper.getNotesByUserId(userId);
    }

    public Note getNoteById(int noteId, int userId) {
        return this.noteMapper.getNoteByNoteId(noteId, userId);
    }

    public int addNote(Note note) {
        return this.noteMapper.insertNote(note);
    }

    public int updateNote(Note note, int userId) {
        return this.noteMapper.updateNote(note, userId);
    }

    public void deleteNote(int noteId, int userId) {
        this.noteMapper.deleteNote(noteId, userId);
    }
}
