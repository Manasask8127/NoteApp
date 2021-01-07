package com.example.mvvmarchitectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class,version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private NoteDao noteDao;

        private PopulateAsyncTask(NoteDatabase db)
        {
            noteDao=db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            noteDao.insert(new Note("title1","Description1",1));
            noteDao.insert(new Note("title2","Description2",2));
            noteDao.insert(new Note("title3","Description3",3));
            return null;
        }
    }

}

