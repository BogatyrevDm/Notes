package com.example.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SingleNoteFragment extends Fragment {

    static final String ARG_SINGLE_NOTE = "note";
    private static final int REQUEST_CODE = 99;
    static final String DATE_EXTRA = "dateForPicker";

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
        TextView tvDate = view.findViewById(R.id.text_view_date);
        tvDate.setText(note.getCreationDate());
        tvDate.setOnClickListener(v -> {
            Context context = getContext();
            if (context != null) {
                Intent intent = new Intent(context, DataPickerActivity.class);

                intent.putExtra(DATE_EXTRA, note.getCreationDate());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        TextView tvDescription = view.findViewById(R.id.text_view_description);
        tvDescription.setText(note.getDescription());
        TextView tvContent = view.findViewById(R.id.edit_text_content);
        tvContent.setText(note.getContent());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        //Если нажата кнопка ОК - установим дату
        if (resultCode == Activity.RESULT_OK) {
            String dateStr = data.getExtras().getString(DATE_EXTRA);
            note.setCreationDate(dateStr);
            Activity activity = getActivity();
            if (activity != null) {
                TextView tvDate = activity.findViewById(R.id.text_view_date);
                tvDate.setText(dateStr);
            }
        }

    }
}