package com.example.ccaucott.roomwords;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Word.class}, version = 2)
public abstract class WordRoomDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    private static WordRoomDatabase INSTANCE;
    
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        /**
         * Called when the database has been opened.
         *
         * @param db The database.
         */
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static WordRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (WordRoomDatabase.class){
                if (INSTANCE == null){
                    // Crear BD...
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordRoomDatabase.class, "word_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mDao;
        String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (mDao.getAnyWord().length < 1){
                for (int i = 0; i <= words.length - 1; i++) {
                    Word word = new Word(words[i]);
                    mDao.insert(word);
                }
            }
            return null;
        }
    }
}
