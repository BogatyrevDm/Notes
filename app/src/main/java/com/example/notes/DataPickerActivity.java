package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DataPickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_picker);
        initView();
    }

    private void initView() {
        initDatePicker();
        initButtons();
    }

    //Инициализируем кнопки
    private void initButtons() {
        Button buttonOk = findViewById(R.id.button_ok_date_picker);

        buttonOk.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent();
            DatePicker picker = (DatePicker) findViewById(R.id.date_picker);
            Calendar calendar = new GregorianCalendar(picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth());
            long dateUT = calendar.getTimeInMillis();
            intent.putExtra(SingleNoteFragment.DATE_EXTRA, dateUT);
            setResult(RESULT_OK, intent);
            finish();
        });
        Button buttonCancel = findViewById(R.id.button_cancel_date_picker);
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
    }

    //Инициализируем DatePicker
    private void initDatePicker() {
        DatePicker picker = (DatePicker) findViewById(R.id.date_picker);
        long dateUT = getIntent().getExtras().getLong(SingleNoteFragment.DATE_EXTRA);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) dateUT));
        picker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)-1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}