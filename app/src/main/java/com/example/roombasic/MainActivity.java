package com.example.roombasic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roombasic.room.Word;

public class MainActivity extends AppCompatActivity {

    public static WordViewModel viewModel;
    RecyclerView recyclerView;
    WordAdapter wordAdapter;
    public static int width;

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;

        recyclerView = findViewById(R.id.recyclerView);
        wordAdapter = new WordAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setAdapter(wordAdapter);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this))
                .get(WordViewModel.class);

        viewModel.getAllWords().observe(this, words -> {
            wordAdapter.setWords(words);
            wordAdapter.notifyDataSetChanged();
        });

    }

    String englishWord, chineseMeaning;
    public void addWord(View v) {
        if (WordAdapter.isDisplay && !WordAdapter.animateLock) {
            LinearLayout linearLayout = WordAdapter.lastDisplay.findViewById(R.id.linearLayout);
            Animation animation = new TranslateAnimation(0, linearLayout.getWidth(), 0, 0);
            animation.setDuration(300);
            animation.setRepeatCount(0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    WordAdapter.animateLock = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linearLayout.setX(width);
                    WordAdapter.isDisplay = false;
                    WordAdapter.animateLock = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            linearLayout.startAnimation(animation);
        }
        View view = LayoutInflater.from(this)
                .inflate(R.layout.word_edit, null);
        EditText englishWordEditText = view.findViewById(R.id.englishWordEditText);
        EditText chineseMeaningEditText = view.findViewById(R.id.chineseMeaningEditText);
        if (englishWord != null || chineseMeaning != null) {
            englishWordEditText.setText(englishWord);
            chineseMeaningEditText.setText(chineseMeaning);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("添加单词")
                .setView(view)
                .setNegativeButton("提交", (dialog, which) -> {
                    englishWord = englishWordEditText.getText().toString();
                    chineseMeaning = chineseMeaningEditText.getText().toString();
                    if (englishWord.trim().compareTo("") != 0 && chineseMeaning.trim().compareTo("") != 0) {
                        viewModel.insertWords(new Word(englishWord, chineseMeaning));
                        englishWord = null;
                        chineseMeaning = null;
                        Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "添加失败！输入框为空。", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("取消", (dialog, which) -> dialog.cancel())
                .setOnCancelListener(dialog -> {
                    englishWord = englishWordEditText.getText().toString();
                    chineseMeaning = chineseMeaningEditText.getText().toString();
                })
                .create();
        alertDialog.show();
    }

    public void removeAllWords(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("是否清空？")
                .setNegativeButton("清空", (dialog, which) -> {
                    viewModel.deleteAllWords();
                    WordAdapter.isDisplay = false;
                    WordAdapter.lastDisplay = null;
                })
                .setPositiveButton("取消", (dialog, which) -> {
                })
                .create();
        alertDialog.show();
    }
}