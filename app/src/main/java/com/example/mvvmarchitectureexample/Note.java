package com.example.mvvmarchitectureexample;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String note;
    private String description;
    private int priority;

    public Note(String note, String description, int priority) {
        this.note = note;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
