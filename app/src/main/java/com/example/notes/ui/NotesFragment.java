package com.example.notes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.FragmentHandler;
import com.example.notes.MainActivity;
import com.example.notes.R;
import com.example.notes.data.Note;
import com.example.notes.data.NoteSource;
import com.example.notes.data.NoteSourceFirebaseImpl;
import com.example.notes.data.NoteSourceResponseDelete;
import com.example.notes.observe.Observer;
import com.example.notes.observe.Publisher;

public class NotesFragment extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private static final String DIALOG_TAG = "dialog";
    private int currentNoteInt = 0;
    private NoteSource notesSource;
    private boolean isLandscape;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Publisher publisher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) requireContext();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        publisher = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);
        initList(view);
        notesSource = new NoteSourceFirebaseImpl().init(notesData -> {
            recyclerViewAdapter.notifyDataSetChanged();
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
        });
        recyclerViewAdapter.setDataSource(notesSource);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    //Инициализируем интерфейс
    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerViewAdapter.setOnItemClickListener(position -> {
            showNote(getNote(position));
            currentNoteInt = position;
            publisher.subscribe(new Observer() {
                @Override
                public void updateNotes(Note note) {
                    notesSource.updateNote(position, note, () -> {
                        if (isLandscape) {
                            currentNoteInt = position;
                            recyclerViewAdapter.notifyItemChanged(position);
                        }
                    });

                }
            });
        });
        recyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_context, menu);
    }

    private Note getNote(int position) {
        return notesSource.getNote(position);
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
        SingleNoteFragment detail;
        if (currentNote == null) {
            detail = SingleNoteFragment.newInstance();
        } else {
            detail = SingleNoteFragment.newInstance(currentNote);
        }
        FragmentHandler.replaceFragment(requireActivity(), detail, R.id.single_note, false, false, false);
    }

    //Покажем содержимое заметки для портретного режима
    private void showNotePort(Note currentNote) {
        Context context = getContext();
        if (context != null) {
            SingleNoteFragment detail;
            if (currentNote == null) {
                detail = SingleNoteFragment.newInstance();
            } else {
                detail = SingleNoteFragment.newInstance(currentNote);
            }
            FragmentHandler.replaceFragment(requireActivity(), detail, R.id.notes, true, false, false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_NOTE, currentNoteInt);
        super.onSaveInstanceState(outState);
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
                showNote(null);
                publisher.subscribe(note -> notesSource.addNote(note, () -> {
                    if (isLandscape) {
                        currentNoteInt = notesSource.size() - 1;
                        recyclerViewAdapter.notifyItemInserted(currentNoteInt);
                    }
                }));
                return true;
            case R.id.clear_notes:
                DialogFragment dialogFragment = DialogFragment.newInstance(getString(R.string.clear_notes_text));
                dialogFragment.setOnDialogListener(() -> {
                            notesSource.clearNotes(() -> {
                                currentNoteInt = 0;
                                recyclerViewAdapter.notifyDataSetChanged();
                            });
                        }
                );
                dialogFragment.show(requireFragmentManager(), DIALOG_TAG);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = recyclerViewAdapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.delete_note:
                final int positionFi = position;
                DialogFragment dialogFragment = DialogFragment.newInstance(getString(R.string.delete_note_text));
                dialogFragment.setOnDialogListener(() -> {
                    notesSource.deleteNote(positionFi, new NoteSourceResponseDelete() {
                        @Override
                        public void deleted() {
                            recyclerViewAdapter.notifyItemRemoved(positionFi);
                        }
                    });

                    //для ландшафтной ориентации проверим размер списка.
                    if (isLandscape && notesSource.size() > 0) {
                        //Если удалили позицию в конце списка, откроем по умолчанию предыдущую позицию
                        //TODO Обработать открытие детально заметки при очистке списка.
                        //Сейчас открывается последняя удаленная

                        if (notesSource.size() <= positionFi) {
                            showNoteLand(notesSource.getNote(positionFi - 1));
                        } else {
                            showNoteLand(notesSource.getNote(positionFi));
                        }
                    }
                });
                dialogFragment.show(requireFragmentManager(), DIALOG_TAG);
                return true;
        }
        return super.onContextItemSelected(item);
    }
}