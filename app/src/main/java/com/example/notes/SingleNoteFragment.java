package com.example.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleNoteFragment extends Fragment {

    static final String ARG_SINGLE_NOTE = "note";
    private Note note;
    public SingleNoteFragment() {
        // Required empty public constructor
    }

    public static SingleNoteFragment newInstance(Note note) {
        SingleNoteFragment fragment = new SingleNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SINGLE_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_SINGLE_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_note, container, false);
        TextView tvName = view.findViewById(R.id.text_view_name);
        tvName.setText(note.getName());
        EditText etDate = view.findViewById(R.id.edit_text_date);
//        String pattern = getResources().getString(R.string.data_format);
//        DateFormat df = new SimpleDateFormat(pattern);
//        String todayAsString = df.format(note.getCreationDate());
        etDate.setText(note.getCreationDate());
        TextView tvDescription = view.findViewById(R.id.text_view_description);
        tvDescription.setText(note.getDescription());
        TextView tvContent = view.findViewById(R.id.edit_text_content);
        tvContent.setText(note.getContent());
        return view;
    }
}