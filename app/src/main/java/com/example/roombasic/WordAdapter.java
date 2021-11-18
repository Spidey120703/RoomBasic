package com.example.roombasic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roombasic.room.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    List<Word> words = new ArrayList<>();

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public static boolean isDisplay = false;
    @SuppressLint("StaticFieldLeak")
    public static View lastDisplay;
    public static boolean animateLock = false;

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.word_item, parent, false);
        itemView.setOnClickListener(v -> {
            if (isDisplay && !animateLock) {
                LinearLayout linearLayout = lastDisplay.findViewById(R.id.linearLayout);
                Animation animation = new TranslateAnimation(0, linearLayout.getWidth(), 0, 0);
                animation.setDuration(300);
                animation.setRepeatCount(0);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        animateLock = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linearLayout.setX(MainActivity.width);
                        isDisplay = false;
                        animateLock = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                linearLayout.startAnimation(animation);
            } else if (!isDisplay && !animateLock) {
                lastDisplay = v;
                LinearLayout linearLayout = lastDisplay.findViewById(R.id.linearLayout);
                linearLayout.setX(MainActivity.width - linearLayout.getWidth());
                Animation animation = new TranslateAnimation(MainActivity.width - linearLayout.getWidth(), 0, 0, 0);
                animation.setDuration(300);
                animation.setRepeatCount(0);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        animateLock = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animateLock = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                linearLayout.startAnimation(animation);
                isDisplay = true;
            }
        });
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = words.get(position);
//        holder.idTextView.setText(String.valueOf(word.getId()));
        holder.idTextView.setText(String.valueOf(position + 1));
        holder.englishWordTextView.setText(word.getWord());
        holder.chineseMeaningTextView.setText(word.getMeaning());
        holder.itemView.findViewById(R.id.editWordButton).setOnClickListener(v -> {
            View view = LayoutInflater.from(v.getContext())
                    .inflate(R.layout.word_edit, null);
            EditText englishWordEditText = view.findViewById(R.id.englishWordEditText);
            EditText chineseMeaningEditText = view.findViewById(R.id.chineseMeaningEditText);
            englishWordEditText.setText(word.getWord());
            chineseMeaningEditText.setText(word.getMeaning());
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle("编辑单词")
                    .setView(view)
                    .setNegativeButton("提交", (dialog, which) -> {
                        word.setWord(englishWordEditText.getText().toString());
                        word.setMeaning(chineseMeaningEditText.getText().toString());
                        MainActivity.viewModel.updateWords(word);
                        Toast.makeText(v.getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                    })
                    .setPositiveButton("取消", (dialog, which) -> {
                    })
                    .create();
            alertDialog.show();
//                closeButtons();
        });
        holder.itemView.findViewById(R.id.deleteWordButton).setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle("是否删除此项？")
                    .setNegativeButton("删除", (dialog, which) -> {
                        if (isDisplay && !animateLock) {
                            LinearLayout linearLayout = lastDisplay.findViewById(R.id.linearLayout);
                            Animation animation = new TranslateAnimation(0, linearLayout.getWidth(), 0, 0);
                            animation.setDuration(300);
                            animation.setRepeatCount(0);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    animateLock = true;
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    linearLayout.setX(MainActivity.width);
                                    isDisplay = false;
                                    MainActivity.viewModel.deleteWords(word);
                                    animateLock = false;
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                            linearLayout.startAnimation(animation);
                        }
                    })
                    .setPositiveButton("取消", (dialog, which) -> {
                    })
                    .create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, englishWordTextView, chineseMeaningTextView;
        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            englishWordTextView = itemView.findViewById(R.id.englishWordTextView);
            chineseMeaningTextView = itemView.findViewById(R.id.chineseMeaningTextView);
        }
    }
}
