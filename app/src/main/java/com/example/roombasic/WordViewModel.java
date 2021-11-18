package com.example.roombasic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roombasic.room.Word;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private final WordRepository repository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
    }

    public void insertWords(Word... words) {
        repository.insertWords(words);
    }

    public void updateWords(Word... words) {
        repository.updateWords(words);
    }

    public void deleteWords(Word... words) {
        repository.deleteWords(words);
    }

    public void deleteAllWords() {
        repository.deleteAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return repository.getAllWords();
    }
}
