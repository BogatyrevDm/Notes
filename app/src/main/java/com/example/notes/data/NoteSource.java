package com.example.notes.data;

public interface NoteSource {
    Note getNote(int position);

    int size();

    void addNote(Note note);

    void updateNote(int position, Note note);

    void deleteNote(int position);

    void clearNotes();
}
