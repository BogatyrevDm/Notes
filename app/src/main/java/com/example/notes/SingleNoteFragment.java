package com.example.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        tvDate.setText(note.getFormatedCreationDate());
        tvDate.setOnClickListener(v -> {
            DataPickerFragment detail = DataPickerFragment.newInstance(note);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            if (isLandscape) {
                fragmentTransaction.replace(R.id.single_note, detail);
            } else {
                fragmentTransaction.replace(R.id.notes, detail);
            }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        });

        TextView tvDescription = view.findViewById(R.id.text_view_description);
        tvDescription.setText(note.getDescription());
        TextView tvContent = view.findViewById(R.id.edit_text_content);
        tvContent.setText(note.getContent());
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_single_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.send_note:
                Toast.makeText(getContext(), "Send chosen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.add_photo:
                Toast.makeText(getContext(), "Add photo chosen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.add_link:
                Toast.makeText(getContext(), "Add link chosen", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        //Если нажата кнопка ОК - установим дату
        if (resultCode == Activity.RESULT_OK) {
            note.setCreationDateUnixTime(data.getExtras().getLong(DATE_EXTRA));
            Activity activity = getActivity();
            if (activity != null) {
                TextView tvDate = activity.findViewById(R.id.text_view_date);
                tvDate.setText(note.getFormatedCreationDate());
            }
        }

    }
}