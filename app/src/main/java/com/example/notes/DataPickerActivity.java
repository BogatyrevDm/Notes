package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class DataPickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_picker);
        String dateStr = getIntent().getExtras().getString(SingleNoteFragment.DATE_EXTRA);
        DatePicker picker = (DatePicker) findViewById(R.id.date_picker);
        String[] arr = dateStr.split(getString(R.string.date_separator));
        picker.updateDate((int) Integer.parseInt(arr[0]), (int) Integer.parseInt(arr[1])-1, (int) Integer.parseInt(arr[2]));

        Button buttonOk = findViewById(R.id.button_ok_date_picker);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                DatePicker picker = (DatePicker) findViewById(R.id.date_picker);
                String dateStr = String.format(getString(R.string.date_format),picker.getYear(),picker.getMonth()+1,picker.getDayOfMonth());
                intent.putExtra(SingleNoteFragment.DATE_EXTRA, dateStr);
                setResult(RESULT_OK, intent);
                finish();
            }

        });
        Button buttonCancel = findViewById(R.id.button_cancel_date_picker);
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
    }
}