package com.example.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = createNewNote(0);
        }
        if (isLandscape){
            showNoteLand(currentNote);
        }
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.names);
        for (int i = 0; i < notes.length; i++) {
            String note = notes[i];
            TextView tv = new TextView(getContext());
            tv.setText(note);
            layoutView.addView(tv);
            final int fi = i;
            tv.setOnClickListener(v -> {
                currentNote = createNewNote(fi);
                showNote(currentNote);
            });
        }
    }

    private Note createNewNote(int index) {
        Resources res = getResources();
        String stringDate = res.getStringArray(R.array.dates)[index];
//        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.data_format));
//        Date date = new Date();
//        try {
//            date = df.parse(stringDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        int isImportantInt = Integer.parseInt(res.getStringArray(R.array.importances)[index]);
        Boolean isImportant = isImportantInt == 1;
        return new Note(res.getStringArray(R.array.names)[index],
                res.getStringArray(R.array.descriptions)[index], res.getStringArray(R.array.dates)[index], isImportant, res.getStringArray(R.array.contents)[index]);
    }

    private void showNote(Note currentNote) {
        if (isLandscape) {
            showNoteLand(currentNote);
        } else {
            showNotePort(currentNote);
        }
    }

    private void showNoteLand(Note currentNote) {
        SingleNoteFragment detail = SingleNoteFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.single_note, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showNotePort(Note currentNote) {
        Intent intent = new Intent(getActivity(), SingleNoteActivity.class);
        intent.putExtra(SingleNoteFragment.ARG_SINGLE_NOTE, currentNote);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }
}