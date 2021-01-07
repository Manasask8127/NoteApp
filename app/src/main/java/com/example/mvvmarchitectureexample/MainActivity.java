package com.example.mvvmarchitectureexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;

    public static final int ADD_REQUEST_CODE=1;
    public static final int EDIT_REQUEST_CODE=2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton floatingActionButton=findViewById(R.id.add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AddOrEditNoteActivity.class);
                startActivityForResult(intent,ADD_REQUEST_CODE);

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(MainActivity.this, AddOrEditNoteActivity.class);
                intent.putExtra(AddOrEditNoteActivity.ID,note.getId());
                intent.putExtra(AddOrEditNoteActivity.TITLE,note.getNote());
                intent.putExtra(AddOrEditNoteActivity.DESCRIPTION,note.getDescription());
                intent.putExtra(AddOrEditNoteActivity.PRIORITY,note.getPriority());

                startActivityForResult(intent,EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_REQUEST_CODE && resultCode==RESULT_OK)
        {
            String title=data.getStringExtra(AddOrEditNoteActivity.TITLE);
            String description=data.getStringExtra(AddOrEditNoteActivity.DESCRIPTION);
            int priority=data.getIntExtra(AddOrEditNoteActivity.PRIORITY,1);

            Note note=new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this,"Saved!",Toast.LENGTH_LONG).show();
        }
        else if(requestCode==EDIT_REQUEST_CODE && resultCode==RESULT_OK)
        {
            int id=data.getIntExtra(AddOrEditNoteActivity.ID,-1);
            if(id==-1)
            {
                Toast.makeText(this,"CAn't update",Toast.LENGTH_LONG).show();
                return;
            }
            String title=data.getStringExtra(AddOrEditNoteActivity.TITLE);
            String description=data.getStringExtra(AddOrEditNoteActivity.DESCRIPTION);
            int priority=data.getIntExtra(AddOrEditNoteActivity.PRIORITY,1);

            Note note=new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this,"Updated!",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Not Saved!",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                noteViewModel.deleteAll();
                Toast.makeText(this, "deleteed all", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}