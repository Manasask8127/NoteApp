package com.example.mvvmarchitectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddOrEditNoteActivity extends AppCompatActivity {
    public static final String ID = "com.example.mvvmarchitectureexample.ID";
    public static final String TITLE = "com.example.mvvmarchitectureexample.TITLE";
    public static final String DESCRIPTION = "com.example.mvvmarchitectureexample.DESCRIPTION";
    public static final String PRIORITY = "com.example.mvvmarchitectureexample.PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edittext_title);
        editTextDescription = findViewById(R.id.edittext_description);
        numberPicker = findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent i = getIntent();
        if (i.hasExtra(ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(i.getStringExtra(TITLE));
            editTextDescription.setText(i.getStringExtra(DESCRIPTION));
            numberPicker.setValue(i.getIntExtra(PRIORITY, 1));
        } else
            setTitle("Add Note");

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter title or description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(TITLE, title);
        data.putExtra(DESCRIPTION, description);
        data.putExtra(PRIORITY, priority);

        int id = getIntent().getIntExtra(ID, -1);
        if (id != -1) {
            data.putExtra(ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}