package com.example.notes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.FragmentHandler;
import com.example.notes.R;
import com.example.notes.data.Note;
import com.example.notes.data.NoteSource;
import com.example.notes.data.NoteSourceImpl;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private int currentNoteInt = 0;
    private NoteSource notesSource;
    private boolean isLandscape;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesSource = new NoteSourceImpl(getResources()).init();
    }

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
            currentNoteInt = savedInstanceState.getInt(CURRENT_NOTE);
        } else {
            currentNoteInt = 0;
        }

        if (isLandscape) {
            showNoteLand(getNote(currentNoteInt));
        }
    }

    //Инициализируем интерфейс
    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerViewAdapter = new RecyclerViewAdapter(notesSource, this);
        recyclerViewAdapter.setOnItemClickListener(position -> showNote(getNote(position)));
        recyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_context, menu);
        //Для ландшафтной ориентации - скроем. В ней окно редактирования открыто по умолчанию.
        menu.findItem(R.id.change_note).setVisible(!isLandscape);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = recyclerViewAdapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.change_note:
                return true;
            case R.id.delete_note:
                notesSource.deleteNote(position);
                recyclerViewAdapter.notifyItemRemoved(position);
                //для ландшафтной ориентации проверим размер списка.
                if (isLandscape && notesSource.size() > 0) {
                    //Если удалили позицию в конце списка, откроем по умолчанию предыдущую позицию
                    if (notesSource.size() <= position) {
                        position--;
                    }
                    showNoteLand(notesSource.getNote(position));
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private Note getNote(int position) {
        return notesSource.getNote(position);
    }

    private Note createNewNote(int index) {
        Resources res = getResources();
        int isImportantInt = Integer.parseInt(res.getStringArray(R.array.importances)[index]);
        Boolean isImportant = isImportantInt == 1;
        Note note = new Note(res.getStringArray(R.array.names)[index],
                res.getStringArray(R.array.descriptions)[index], Long.parseLong(res.getStringArray(R.array.datesUT)[index]), isImportant, res.getStringArray(R.array.contents)[index]);
        return note;
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
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_NOTE, currentNoteInt);
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
            case R.id.clear_notes:
                notesSource.clearNotes();
                recyclerViewAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}