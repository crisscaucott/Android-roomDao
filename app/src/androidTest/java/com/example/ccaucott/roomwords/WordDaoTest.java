package com.example.ccaucott.roomwords;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WordDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WordDao mWordDao;
    private WordRoomDatabase mWordRoomDatabase;

    @Before
    public void initDb(){
        mWordRoomDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                WordRoomDatabase.class)
                .allowMainThreadQueries()
                .build();

        mWordDao = mWordRoomDatabase.wordDao();
    }

    @After
    public void closeDb(){
        mWordRoomDatabase.close();
    }


    @Test
    public void checkWordsOrderPassingGato() throws InterruptedException {
        String[] words = {"dolphin", "crocodile", "cobra"};
        for (int i = 0; i <= words.length - 1 ; i++){
            Word word = new Word(words[i]);
            mWordDao.insert(word);
        }

        List<Word> words2 = LiveDataTestUtil.getValue(mWordDao.getAllWords());

        assertEquals("OK", words[2], words2.get(0).getWord());
        assertEquals("OK", words[1], words2.get(1).getWord());
        assertEquals("OK", words[0], words2.get(2).getWord());


        mWordDao.insert(new Word("Gato"));
        words2 = LiveDataTestUtil.getValue(mWordDao.getAllWords());

        assertEquals("OK", words[2], words2.get(0).getWord());
        assertEquals("OK", words[1], words2.get(1).getWord());
        assertEquals("OK", words[0], words2.get(2).getWord());
        assertEquals("OK", "Gato", words2.get(3).getWord());

    }

}
