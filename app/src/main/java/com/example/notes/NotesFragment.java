package com.example.notes;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NotesFragment extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private Note currentNote;
    private boolean isLandscape;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Установим признак ландшафтной ориентации
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = createNewNote(0);
        }
        if (isLandscape) {
            showNoteLand(currentNote);
        }
    }

    //Инициализируем интерфейс
    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.names);
        for (int i = 0; i < notes.length; i++) {
            String note = notes[i];
            Context context = getContext();
            if (context != null) {
                TextView tv = new TextView(context);
                tv.setText(note);
                layoutView.addView(tv);
                final int fi = i;
                tv.setOnClickListener(v -> {
                    currentNote = createNewNote(fi);
                    showNote(currentNote);
                });
            }
        }
    }

    private Note createNewNote(int index) {
        Resources res = getResources();
        int isImportantInt = Integer.parseInt(res.getStringArray(R.array.importances)[index]);
        Boolean isImportant = isImportantInt == 1;
        return new Note(res.getStringArray(R.array.names)[index],
                res.getStringArray(R.array.descriptions)[index], Long.parseLong(res.getStringArray(R.array.datesUT)[index]), isImportant, res.getStringArray(R.array.contents)[index]);
    }

    //Покажем содержимое заметки
    private void showNote(Note currentNote) {
        if (isLandscape) {
            showNoteLand(currentNote);
        } else {
            showNotePort(currentNote);
        }
    }

    //Покажем содержимое заметки для ландшафтного режима
    private void showNoteLand(Note currentNote) {
        SingleNoteFragment detail = SingleNoteFragment.newInstance(currentNote);
        FragmentHandler.replaceFragment(requireActivity(), detail, R.id.single_note, false);
    }

    //Покажем содержимое заметки для портретного режима
    private void showNotePort(Note currentNote) {
        Context context = getContext();
        if (context != null) {
            SingleNoteFragment detail = SingleNoteFragment.newInstance(currentNote);
            FragmentHandler.replaceFragment(requireActivity(), detail, R.id.notes, true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_note:
                Toast.makeText(getContext(), "Add chosen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sort_notes:
                Toast.makeText(getContext(), "Sort chosen", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}