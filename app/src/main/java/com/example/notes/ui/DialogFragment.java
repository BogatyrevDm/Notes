package com.example.notes.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notes.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class DialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_TITLE_TEXT = "titleText";

    private OnDialogListener onDialogListener;
    private String titleText;

    public void setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }

    public DialogFragment() {
        // Required empty public constructor
    }

    public static DialogFragment newInstance(String titleText) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE_TEXT, titleText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleText = getArguments().getString(ARG_TITLE_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        setCancelable(false);
        TextView textViewTitle = view.findViewById(R.id.dialog_title);
        textViewTitle.setText(titleText);
        view.findViewById(R.id.dialog_button_ok).setOnClickListener(v -> {
            dismiss();
            if (onDialogListener != null) {
                onDialogListener.onDialogOK();
            }
        });
        view.findViewById(R.id.dialog_button_cancel).setOnClickListener(v -> {
            dismiss();
        });
        return view;
    }
}