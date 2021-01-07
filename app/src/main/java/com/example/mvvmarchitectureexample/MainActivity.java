package com.example.mvvmarchitectureexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;

    public static final int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton floatingActionButton=findViewById(R.id.add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddNote.class);
                startActivityForResult(intent,REQUEST_CODE);

            }
        });

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update recyclerview
                adapter.setNotes(notes);
                Toast.makeText(getApplicationContext(),"onchanged",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK)
        {
            String title=data.getStringExtra(AddNote.TITLE);
            String description=data.getStringExtra(AddNote.DESCRIPTION);
            int priority=data.getIntExtra(AddNote.PRIORITY,1);

            Note note=new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this,"Saved!",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Not Saved!",Toast.LENGTH_LONG).show();
    }
}