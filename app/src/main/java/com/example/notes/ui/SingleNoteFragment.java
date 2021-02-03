package com.example.notes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.notes.FragmentHandler;
import com.example.notes.MainActivity;
import com.example.notes.R;
import com.example.notes.data.Note;
import com.example.notes.observe.Publisher;

public class SingleNoteFragment extends Fragment {

    static final String ARG_SINGLE_NOTE = "note";
    private Publisher publisher;
    private Note note;
    EditText etName;
    TextView tvDate;
    EditText etDescription;
    EditText etContent;
    Button buttonOk;
    Button buttonCancel;

    public SingleNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    public static SingleNoteFragment newInstance(Note note) {
        SingleNoteFragment fragment = new SingleNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SINGLE_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static SingleNoteFragment newInstance() {
        SingleNoteFragment fragment = new SingleNoteFragment();
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
        setHasOptionsMenu(true);

        initView(view);
        if (note != null) {
            populateView();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.unsubscribeAll();
    }

    private Note collectNote() {
        String name = this.etName.getText().toString();
        String description = this.etDescription.getText().toString();
        Boolean isImportant = true;
        String content = this.etContent.getText().toString();
        return new Note(name, description, (long) 0.0, isImportant, content);
    }

    private void initView(View view) {
        etName = view.findViewById(R.id.edit_text_name);
        tvDate = view.findViewById(R.id.text_view_date);

        etDescription = view.findViewById(R.id.edit_text_description);
        etContent = view.findViewById(R.id.edit_text_content);
        tvDate.setOnClickListener(v -> {
            DataPickerFragment detail = DataPickerFragment.newInstance(note);
            FragmentActivity activity = requireActivity();
            FragmentHandler.replaceFragment(activity, detail, FragmentHandler.getIdFromOrientation(activity), true);

        });

        //Пока оба листенера будут делать одно и тоже. Разветвлю логику,
        // когда буду реализовывать сохранение измененных заметок
        buttonOk = view.findViewById(R.id.button_ok_single_note);
        buttonOk.setOnClickListener(v -> {
                    note = collectNote();
                    publisher.notifySingle(note);
                    popBackStackIfNotLand();
                }
        );
        buttonCancel = view.findViewById(R.id.button_cancel_single_note);
        buttonCancel.setOnClickListener(v -> popBackStackIfNotLand());
    }

    private void populateView() {
        etName.setText(note.getName());
        tvDate.setText(note.getFormatedCreationDate());
        etDescription.setText(note.getDescription());
        etContent.setText(note.getContent());
    }

    //Выкидываем активность из стека, когда ориентация портретная
    private void popBackStackIfNotLand() {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            FragmentHandler.popBackStack(requireActivity());
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
}