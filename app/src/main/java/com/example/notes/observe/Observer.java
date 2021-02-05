package com.example.notes.observe;

import com.example.notes.data.Note;

public interface Observer {
    void updateNotes(Note note);
}
