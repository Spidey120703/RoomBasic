package com.example.roombasic;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.roombasic.room.Word;
import com.example.roombasic.room.WordDao;
import com.example.roombasic.room.WordDatabase;

import java.util.List;

@SuppressWarnings("ALL")
public class WordRepository {

    LiveData<List<Word>> wordsLiveData;
    WordDao dao;

    public WordRepository(Application application) {
        WordDatabase database = WordDatabase.getDatabase(application.getApplicationContext());
        dao = database.getWordDao();
        wordsLiveData = dao.getAllWordsLiveData();
    }

    public void insertWords(Word... words) {
        new InsertWordsAsync(dao).execute(words);
    }

    public void updateWords(Word... words) {
        new UpdateWordsAsync(dao).execute(words);
    }

    public void deleteWords(Word... words) {
        new DeleteWordsAsync(dao).execute(words);
    }

    public void deleteAllWords() {
        new DeleteAllWordsAsync(dao).execute();
    }

    public LiveData<List<Word>> getAllWords() {
        return wordsLiveData;
    }

    static class InsertWordsAsync extends AsyncTask<Word, Void, Void> {
        private final WordDao dao;

        InsertWordsAsync(WordDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            dao.insertWords(words);
            return null;
        }
    }

    static class UpdateWordsAsync extends AsyncTask<Word, Void, Void> {
        private final WordDao dao;

        UpdateWordsAsync(WordDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            dao.updateWords(words);
            return null;
        }
    }

    static class DeleteWordsAsync extends AsyncTask<Word, Void, Void> {
        private final WordDao dao;

        DeleteWordsAsync(WordDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            dao.deleteWords(words);
            return null;
        }
    }

    static class DeleteAllWordsAsync extends AsyncTask<Void, Void, Void> {
        private final WordDao dao;

        DeleteAllWordsAsync(WordDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllWords();
            return null;
        }
    }

}
