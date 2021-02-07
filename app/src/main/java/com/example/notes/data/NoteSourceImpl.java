package com.example.notes.data;

import android.content.res.Resources;

import com.example.notes.R;

import java.util.ArrayList;

public class NoteSourceImpl implements NoteSource {
    private ArrayList<Note> notes;
    private Resources resources;

    public NoteSourceImpl(Resources resources) {
        this.notes = new ArrayList<>(3);
        this.resources = resources;
    }

    public NoteSource init(NoteSourceResponse noteSourceResponse) {
        String[] notesArray = resources.getStringArray(R.array.names);
        for (int i = 0; i < notesArray.length; i++) {
            notes.add(createNewNote(i));
        }
        if (noteSourceResponse != null){
            noteSourceResponse.initialized(this);
        }
        return this;
    }

    private Note createNewNote(int index) {
        int isImportantInt = Integer.parseInt(resources.getStringArray(R.array.importances)[index]);
        Boolean isImportant = isImportantInt == 1;
        Note note = new Note(resources.getStringArray(R.array.names)[index],
                resources.getStringArray(R.array.descriptions)[index], Long.parseLong(resources.getStringArray(R.array.datesUT)[index]), isImportant, resources.getStringArray(R.array.contents)[index]);
        return note;
    }
    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        return notes.size();
    }

    @Override
    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public void updateNote(int position, Note note) {
        notes.set(position, note);
    }

    @Override
    public void deleteNote(int position) {
        notes.remove(position);
    }

    @Override
    public void clearNotes() {
        notes.clear();
    }
}
