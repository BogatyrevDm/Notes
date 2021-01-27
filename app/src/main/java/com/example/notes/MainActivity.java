package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // TODO: Переделать на сингл-активити (продумать передачу данных между фрагментами через обзервер)
    // TODO: Сделать верхнее меню
    // TODO: Сделать боковое меню
    // TODO: финальное тестирование Date unix
    // TODO: финальное тестирование сингл-активити
    // TODO: финальное тестирование меню


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        if (savedInstanceState == null) {
            NotesFragment details = new NotesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.notes, details);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}