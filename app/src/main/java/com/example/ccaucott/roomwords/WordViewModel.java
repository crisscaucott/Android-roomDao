package com.example.ccaucott.roomwords;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;
    private MutableLiveData<Long> mLastWordInserted;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
        mLastWordInserted = mRepository.getLastWordInserted();
    }

    LiveData<Long> getLastWordInserted(){
        return mLastWordInserted;
    }

    LiveData<List<Word>> getAllWords(){
        return mAllWords;
    }

    public void insert(Word word){
        mRepository.insert(word);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void deleteWord(Word word){
        mRepository.deleteWord(word);
    }

    public void updateWord(Word word){
        mRepository.updateWord(word);
    }
}
